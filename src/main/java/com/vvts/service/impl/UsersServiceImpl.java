package com.vvts.service.impl;

import com.vvts.dto.KycUpdateResponseDto;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.dto.UserKycDetailDto;
import com.vvts.dto.UserKycUpdateDto;
import com.vvts.entity.Role;
import com.vvts.entity.Users;
import com.vvts.projection.InitProjection;
import com.vvts.projection.UserBasicProjection;
import com.vvts.projection.UserDetailProjection;
import com.vvts.repo.AccessTokenRepo;
import com.vvts.repo.RoleRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.service.UsersService;
import com.vvts.utiles.ImageUtils;
import com.vvts.utiles.ImageValidation;
import global.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepo usersRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;
    private final AccessTokenRepo accessTokenRepo;

    private final ImageValidation imageValidation;

    private final ImageUtils imageUtils;

    private final ResourceLoader resourceLoader;

    @Value("${server.port}")
    private String serverPort;

    @Value("${upload.path}")
    private String uploadDir;

    @Value("${image.fetch.api}")
    private String imageAccessBaseUrl ;


    @Override
    public PublicUserBasicDataDto savePublicUser(PublicUserBasicDataDto publicUserBasicDataDto) {
        publicUserBasicDataDto.setRoleName("Public User");
        if (publicUserBasicDataDto.getId() == null) {
            // check mobile number or email already exits or not
            if (usersRepo.getMobileNumberCount(publicUserBasicDataDto.getMobileNumber()) > 0) {
                throw new RuntimeException("Mobile Number: " + publicUserBasicDataDto.getMobileNumber() + " already exists");
            }
            if (usersRepo.getEmailCount(publicUserBasicDataDto.getEmail()) > 0) {
                throw new RuntimeException("Email Address : " + publicUserBasicDataDto.getEmail() + " already exists");
            }
        } else {
            // check mobile number or email already exits or not
            if (usersRepo.getMobileNumberCount(publicUserBasicDataDto.getMobileNumber()) > 1) {
                throw new RuntimeException("Mobile Number: " + publicUserBasicDataDto.getMobileNumber() + " already exists");
            }
            if (usersRepo.getEmailCount(publicUserBasicDataDto.getEmail()) > 1) {
                throw new RuntimeException("Email Address : " + publicUserBasicDataDto.getEmail() + " already exists");
            }
        }
        // get user role
        Role role = roleRepo.getRoleByRoleName(publicUserBasicDataDto.getRoleName());
        if (role == null) {
            throw new RuntimeException("Invalid Role name : " + publicUserBasicDataDto.getRoleName() + ".");
        }
        // build entity
        Users users = Users.builder()
                .id(publicUserBasicDataDto.getId())
                .name(publicUserBasicDataDto.getName())
                .email(publicUserBasicDataDto.getEmail())
                .mobileNumber(publicUserBasicDataDto.getMobileNumber())
                .role(role)
                .isEnable(false)
                .password(passwordEncoder.encode(publicUserBasicDataDto.getPassword()))
                .build();
        usersRepo.save(users);
        publicUserBasicDataDto.setId(users.getId());

        return publicUserBasicDataDto;
    }

    @Override
    @Transactional
    public Boolean logoutUser(String userName) {
        // get username of login user
        try {
            accessTokenRepo.deleteAccessTokenByUserName(userName);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public KycUpdateResponseDto updateUserKyc(UserKycUpdateDto userKycUpdateDto) throws IOException {
        // check citizenship number is already exits or not
        if (usersRepo.getVerifiedCitizenshipCount(userKycUpdateDto.getCitizenshipNo()) > 0) {
            throw new RemoteException("Citizenship number : " + userKycUpdateDto.getCitizenshipNo() + " already exists");
        }
        // first validate the three picture pp , citizen font and back
        String ppExtension = imageValidation.validateImage(userKycUpdateDto.getProfilePicture());
        String cfExtension = imageValidation.validateImage(userKycUpdateDto.getCitizenshipFont());
        String cbExtension = imageValidation.validateImage(userKycUpdateDto.getCitizenshipBack());

        // fetch user entity
        Optional<Users> optionalUsers = usersRepo.findById(userKycUpdateDto.getUserId());
        if (!optionalUsers.isPresent()) {
            throw new RemoteException("User does not exists with id: " + userKycUpdateDto.getUserId());
        }
        Users users = optionalUsers.get();
        String profilePictureName = imageUtils.generateUniqueImageName(users.getName(), users.getId(), "profilePicture", ppExtension);
        String citizenshipFontImageName = imageUtils.generateUniqueImageName(users.getName(), users.getId(), "citizenshipfont", cfExtension);
        String citizenshipBackImageName = imageUtils.generateUniqueImageName(users.getName(), users.getId(), "citizenshipback", cbExtension);
        // create folder if not already not exists
        String uploadDir = "";
        Path uploadPath = null;
        Path ppFilePath;
        if (userKycUpdateDto.getProfilePicture() != null) {
            // create folder if not already not exists
            uploadDir = System.getProperty("user.home").concat("/vvts/profile");
            uploadPath = Paths.get(uploadDir);
            // create upload file directory if already not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // create full url
            ppFilePath = uploadPath.resolve(profilePictureName);
            userKycUpdateDto.getProfilePicture().transferTo(ppFilePath);
            // set save url to users table
            users.setProfileImageUrl(String.valueOf(ppFilePath));
        }

        if (userKycUpdateDto.getCitizenshipFont() != null) {
            // create folder if not already not exists
            uploadDir = System.getProperty("user.home").concat("/vvts/citizen");
            uploadPath = Paths.get(uploadDir);
            // create upload file directory if already not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // create full url
            Path cfFilePath = uploadPath.resolve(citizenshipFontImageName);
            userKycUpdateDto.getCitizenshipFont().transferTo(cfFilePath);
            // set save url to users table
            users.setCitizenshipFontUrl(String.valueOf(cfFilePath));
        }

        if (userKycUpdateDto.getCitizenshipBack() != null) {

            // create folder if not already not exists
            uploadDir = System.getProperty("user.home").concat("/vvts/citizen");
            uploadPath = Paths.get(uploadDir);
            // create upload file directory if already not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // create full url
            Path cbFilePath = uploadPath.resolve(citizenshipBackImageName);
            userKycUpdateDto.getCitizenshipBack().transferTo(cbFilePath);
            // set save url to users table
            users.setCitizenshipBackUrl(String.valueOf(cbFilePath));
        }
        users.setCitizenshipNo(userKycUpdateDto.getCitizenshipNo());
        users.setIsNewKycRequest(true);
        users.setAddress(userKycUpdateDto.getAddress());
        users = usersRepo.save(users);

        // make response dto
        return KycUpdateResponseDto.builder()
                .userId(users.getId())
                .name(users.getName())
                .citizenshipNo(users.getCitizenshipNo())
                .citizenshipFontUrl(users.getCitizenshipFontUrl())
                .citizenshipBackUrl(users.getCitizenshipBackUrl())
                .profilePictureUrl(users.getProfileImageUrl()).build();
    }

    @Override
    public List<UserKycDetailDto> getNewKycRequest() {
        List<UserDetailProjection> userDetailList = usersRepo.getNewKycRequestUserData();
        List<UserKycDetailDto> userKycDetailDtoList = new ArrayList<>();
        for (UserDetailProjection user : userDetailList) {
            UserKycDetailDto userKycDetailDto = UserKycDetailDto.builder()
                    .userId(user.getUserId())
                    .name(user.getName())
                    .address(user.getAddress())
                    .email(user.getEmail())
                    .contact(user.getContact())
                    .citizenshipNo(user.getCitizenshipNo())
                    .profilePictureUrl(imageAccessBaseUrl.concat("/profile/").concat(user.getProfilePictureUrl().split("/")
                            [user.getProfilePictureUrl().split("/").length - 1]))
                    .citizenshipFontUrl(imageAccessBaseUrl.concat("/citizen/").concat(user.getCitizenshipFontUrl().split("/")
                            [user.getCitizenshipFontUrl().split("/").length - 1]))
                    .citizenshipBackUrl(imageAccessBaseUrl.concat("/citizen/").concat(user.getCitizenshipBackUrl().split("/")
                            [user.getCitizenshipBackUrl().split("/").length - 1]))
                    .build();
            userKycDetailDtoList.add(userKycDetailDto);

        }
        return userKycDetailDtoList;
    }

    @Override
    public List<InitProjection> getRoleModuleMappingDetail(Integer roleId) {
        return roleRepo.getUserModuleAccess(roleId);
    }

    @Override
    public UserBasicProjection getUserByUserId(Integer userId) {
        return usersRepo.getUsersByUserId(userId);
    }

    @Transactional
    @Override
    public String getTakeActionOnKycRequest(Integer userId, String actionType) {
        if (actionType.equalsIgnoreCase("accept")) {
            usersRepo.getAcceptUserKyc(userId);
            return "Kyc Request Accept Successfully.";
        }
        if (actionType.equalsIgnoreCase("reject")) {
            usersRepo.getRejectUserKyc(userId);
            return "Kyc Request Reject Successfully.";
        }
        return null;
    }

    @Override
    public String getProfileImagePathOfLoginUser(Integer loginUserId) {
        Optional<Users> optionalUsers = usersRepo.findById(loginUserId);
        if (!optionalUsers.isPresent()) {
            throw new RuntimeException("User does not exists with user id : " + loginUserId);
        }
        Users users = optionalUsers.get();
        return users.getProfileImageUrl();
    }

    @Override
    public String getGenerateQrCode(Integer loginUserId) throws IOException {
        String qrCodeImageName = getQrcodeNameByLoginUser(loginUserId);
        String imageFilePath = QRCodeGenerator.getQrCode("", qrCodeImageName);
        return imageFilePath;
    }

    @Override
    public ResponseEntity<Resource> downloadImage(Integer loginUserId) {
        String qrCodeImageName = getQrcodeNameByLoginUser(loginUserId);
        String imagePath = System.getProperty("user.home").concat("/vvts/qr_code/".concat(qrCodeImageName));

        // Load the image file as a Resource
        Resource imageResource = resourceLoader.getResource("file:" + imagePath);
        if (!imageResource.exists()) {
            throw new RuntimeException("image doses not exists");
        }
        // Set the appropriate headers and return the image as a ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(qrCodeImageName).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageResource);
    }

    private String getQrcodeNameByLoginUser(Integer loginUserId) {
        Optional<Users> optionalUsers = usersRepo.findById(loginUserId);
        if (!optionalUsers.isPresent()) {
            throw new RuntimeException("User does not exists with user id : " + loginUserId);
        }
        Users users = optionalUsers.get();
        String qrCodeImageName = imageUtils.generateUniqueImageName(users.getName(), users.getId(), "qrcode", "png");
        return qrCodeImageName;
    }

}
