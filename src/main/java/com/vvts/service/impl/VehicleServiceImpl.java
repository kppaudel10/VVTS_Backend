package com.vvts.service.impl;

import com.vvts.dto.*;
import com.vvts.entity.OwnershipTransfer;
import com.vvts.entity.PinCode;
import com.vvts.entity.Users;
import com.vvts.entity.VehicleDetail;
import com.vvts.enums.VehicleType;
import com.vvts.projection.BuyRequestProjection;
import com.vvts.projection.BuyerRequestProjection;
import com.vvts.projection.NumberPlateScannerProjection;
import com.vvts.projection.OwnershipRequestProjection;
import com.vvts.repo.OwnershipTransferRepo;
import com.vvts.repo.PinCodeRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.VehicleService;
import com.vvts.utiles.*;
import global.MailSend;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepo vehicleRepo;

    private final UsersRepo usersRepo;

    private final VINGenerator vinGenerator;

    private final OwnershipTransferRepo ownershipTransferRepo;

    private final ImageValidation imageValidation;

    private final ImageUtils imageUtils;

    private final ImageScanner imageScanner;

    private final PinCodeRepo pinCodeRepo;

    @Value("${image.fetch.api}")
    private String imageAccessBaseUrl;


    @Override
    public VehicleDto saveVehicleDetail(VehicleDto vehicleDto, Integer loginUserId) {
        // get user by login user id
        Optional<Users> optionalUsers = usersRepo.findById(loginUserId);
        if (!optionalUsers.isPresent()) {
            throw new RuntimeException("User does not exits with user id : " + loginUserId);
        }
        Users users = optionalUsers.get();

        VehicleDetail vehicleDetail = VehicleDetail
                .builder().id(vehicleDto.getId())
                .manufactureYear(vehicleDto.getManufactureYear())
                .vehicleType(VehicleType.getVehicleTypeKey(vehicleDto.getVehicleType()))
                .vehicleIdentificationNo(vinGenerator.generateVIN(vehicleDto.getCompanyCode(),
                        vehicleDto.getVehicleType(), vehicleDto.getManufactureYear()))
                .companyCode(vehicleDto.getCompanyCode())
                .companyName(vehicleDto.getCompanyName())
                .vendor(users).build();
        // save
        vehicleDetail = vehicleRepo.save(vehicleDetail);
        vehicleDto.setId(vehicleDetail.getId());
        vehicleDto.setIdentificationNo(vehicleDetail.getVehicleIdentificationNo());
        return vehicleDto;
    }

    @Override
    public BuyRequestPojo saveVehicleBuyRequest(BuyRequestPojo buyRequestPojo, Integer loginUserId) throws EmailException {
        // first check the owner name and mobile number are valid or not
        Users users = validateUser(buyRequestPojo, loginUserId);
        //check user already applied for vehicle buy request or not
        // build entity
        VehicleDetail vehicleDetail = vehicleRepo.getVehicleDetailByVehicleIdentificationNo(buyRequestPojo.
                getVehicleIdentificationNo());
        if (vehicleDetail == null) {
            throw new RuntimeException("Invalid Vehicle Identification Number : " + buyRequestPojo.getVehicleIdentificationNo());
        }
        Users buyer = new Users();
        buyer.setId(loginUserId);
        OwnershipTransfer ownershipTransfer = OwnershipTransfer.builder()
                .transferRequestDate(new Date())
                .vehicleDetail(vehicleDetail)
                .isApproveByAdmin(false)
                .isApproveByOwner(false)
                .status(1)
                .seller(users)
                .buyer(buyer).build();
        ownershipTransferRepo.save(ownershipTransfer);
        // save pin code
        return buyRequestPojo;
    }

    private Users validateUser(BuyRequestPojo buyRequestPojo, Integer loginUserId) {
        // first check the owner name and mobile number are valid or not
        Users users = usersRepo.getUserMobileNumberCount(buyRequestPojo.getOwnerMobileNumber());
        if (users == null) {
            throw new RuntimeException("Insufficient Owner information.");
        }
        if (!users.getName().equalsIgnoreCase(buyRequestPojo.getOwnerName())) {
            throw new RuntimeException("Invalid Owner name: " + buyRequestPojo.getOwnerName());
        }
        if (users.getId().equals(loginUserId)) {
            throw new RuntimeException("You entered your own information. Please, used different once.");
        }
        // check vehicle identification number is exists or not
        Integer existenceCount = vehicleRepo.getVehicleDetailByVINAndNumber(buyRequestPojo.getVehicleIdentificationNo(),
                users.getCitizenshipNo());
        if (existenceCount == null || existenceCount.equals(0)) {
            throw new RuntimeException("Invalid Vehicle identification number: " + buyRequestPojo.getVehicleIdentificationNo());
        }
        // check if user already applied buy request or not
        Integer count = ownershipTransferRepo.getCountBuyRequest(buyRequestPojo.getVehicleIdentificationNo(),
                loginUserId, users.getId());
        if (count > 0) {
            throw new RuntimeException("Your buy request already on pending.");
        }
        return users;
    }

    private Date getAddMinOnDate(int min) {
        Calendar calendar = Calendar.getInstance();

        // Add 3 minutes
        calendar.add(Calendar.MINUTE, min);

        // Get the updated date and time
        java.util.Date updatedDate = calendar.getTime();

        return updatedDate;
    }

    @Override
    public List<BuyRequestResponseDto> getBuyRequestList(Integer loginUserId) {
        List<BuyRequestProjection> buyerRequestProjectionList = ownershipTransferRepo.getOwnershipTransferByOwnerId(loginUserId);
        List<BuyRequestResponseDto> buyRequestResponseDtoList = new ArrayList<>();
        for (BuyRequestProjection brp : buyerRequestProjectionList) {
            BuyRequestResponseDto buyRequestResponseDto = BuyRequestResponseDto.builder()
                    .id(brp.getId())
                    .requestDate(brp.getRequestDate())
                    .companyCode(brp.getCompanyCode())
                    .manufactureYear(brp.getManufactureYear())
                    .buyerAddress(brp.getBuyerAddress())
                    .buyerName(brp.getBuyerName())
                    .buyerMobileNumber(brp.getBuyerMobileNumber())
                    .buyerAddress(brp.getBuyerAddress())
                    .vehicleIdentificationNo(brp.getVehicleIdentificationNo())
                    .vehicleType(brp.getVehicleType())
                    .buyerEmail(brp.getBuyerEmail())
                    .buyerProfilePictureUrl(imageAccessBaseUrl.concat("/profile/").concat(brp.getBuyerProfileUrl().split("/")
                            [brp.getBuyerProfileUrl().split("/").length - 1])).build();
            //add to list
            buyRequestResponseDtoList.add(buyRequestResponseDto);
        }
        return buyRequestResponseDtoList;
    }

    @Override
    public NumberPlateScannerResponsePojo getScanNumberPlate(MultipartFile scanImage, String destinationLanguage) throws IOException, TesseractException {
        //check language
        if (!(destinationLanguage.equals("eng") || destinationLanguage.equals("nep"))) {
            throw new RuntimeException("Invalid Language Code : " + destinationLanguage);
        }
//        String scanOutput = imageScanner.doOCR(scanImage, destinationLanguage);
        String scanOutput = saveAndScanImage(scanImage,destinationLanguage);
        System.out.println(scanOutput);
        if (scanOutput != null) {
            char[] scanOutputChars = scanOutput.toCharArray();
            String modifiedOutput = scanOutput.replaceAll("\n", "");
            NumberPlateScannerProjection npcp = vehicleRepo.getUserAndVehicleDetailByNumberPlate(modifiedOutput);
            if (npcp != null) {
                NumberPlateScannerResponsePojo scannerResponsePojo = new NumberPlateScannerResponsePojo();
                scannerResponsePojo.setName(npcp.getName());
                scannerResponsePojo.setUserId(npcp.getUserId());
                scannerResponsePojo.setAddress(npcp.getAddress());
                scannerResponsePojo.setContact(npcp.getContact());
                scannerResponsePojo.setEmail(npcp.getEmail());
                scannerResponsePojo.setProfileImageUrl(npcp.getProfileImageUrl());
                scannerResponsePojo.setLicenseValidDate(npcp.getLicenseValidDate());

                return scannerResponsePojo;
            }
        }
        return null;
    }

    @Override
    public boolean generateValidationToken(BuyRequestPojo buyRequestPojo, Integer loginUserId) throws EmailException {
        Users users = validateUser(buyRequestPojo, loginUserId);
        if (users.getIsEnable() == false || users.getIsEnable() == null) {
            throw new RuntimeException("Only verified user can buy and sell their vehicle." +
                    " Please apply for your Kyc verification");
        }
        String token = new RandomCodeGenerator().generateRandomCode(6);
        MailSendDto mailSendDto = new MailSendDto();
        mailSendDto.setEmail(users.getEmail());
        mailSendDto.setUserName(users.getName());
        mailSendDto.setMessage(token);
        new MailSend().sendMail(mailSendDto);
        // check pinCode already exists or not
        PinCode existPinCode = pinCodeRepo.getPinCodeByUserId(loginUserId);
        if (existPinCode == null) {
            // save pin code
            existPinCode = PinCode.builder()
                    .pinCode(token)
                    .users(Users.builder().id(loginUserId).build())
                    .expiredDate(getAddMinOnDate(3)).build();
        } else {
            existPinCode.setPinCode(token);
            existPinCode.setExpiredDate(getAddMinOnDate(3));
        }
        pinCodeRepo.save(existPinCode);
        return true;
    }

    @Override
    @Transactional
    public Boolean validatePincode(String pinCode, Integer loginUserId) {
        PinCode actualToken = pinCodeRepo.getPinCodeByUserId(loginUserId);
        if (actualToken == null || !actualToken.getPinCode().equals(pinCode)) {
            throw new RuntimeException("Invalid PinCode.");
        }
        // delete that token after success message
        pinCodeRepo.deleteById(actualToken.getId());
        return true;
    }

    @Override
    public List<BuyerRequestProjection> getBuyRequestOfLoginUser(Integer loginUserId) {
        return ownershipTransferRepo.getBuyRequestByLoginUser(loginUserId);
    }

    @Override
    @Transactional
    public String takeActionOnSellRequest(SellRequestActionPojo sellRequestActionPojo) {
        String message = null;
        //check action type valid or not
        if (!(sellRequestActionPojo.getActionType().equalsIgnoreCase("accept") ||
                sellRequestActionPojo.getActionType().equalsIgnoreCase("reject"))) {
            throw new RuntimeException("Invalid action type: " + sellRequestActionPojo.getActionType());
        }
        // check action by is valid or not
        if (!(sellRequestActionPojo.getActionBy().equalsIgnoreCase("admin")
                || sellRequestActionPojo.getActionBy().equalsIgnoreCase("owner"))) {
            throw new RuntimeException("Invalid action by : " + sellRequestActionPojo.getActionBy());
        }
        // accept the form
        if (sellRequestActionPojo.getActionType().equalsIgnoreCase("accept")) {
            // if accept by owner
            if (sellRequestActionPojo.getActionBy().equalsIgnoreCase("owner")) {
                ownershipTransferRepo.updateOwnerActionOnOwnershipRequest(1, true, sellRequestActionPojo.getId());
                message = "Vehicle Sell request accept successfully. Further process will be done by admin";
            } else {
//            if (sellRequestActionPojo.getActionBy().equalsIgnoreCase("admin")) {
                ownershipTransferRepo.updateAdminActionOnOwnershipRequest(1, true, sellRequestActionPojo.getId());
                message = "Vehicle Sell request accept successfully";
            }
        } else {
            // if accept by owner
            if (sellRequestActionPojo.getActionBy().equalsIgnoreCase("owner")) {
                ownershipTransferRepo.updateOwnerActionOnOwnershipRequest(0, false, sellRequestActionPojo.getId());
                message = "Vehicle Sell request rejected successfully";
            } else {
//            if (sellRequestActionPojo.getActionBy().equalsIgnoreCase("admin")) {
                ownershipTransferRepo.updateAdminActionOnOwnershipRequest(0, false, sellRequestActionPojo.getId());
                message = "Vehicle Sell request rejected successfully";
            }
        }
        return message;
    }

    @Override
    public List<Map<String, Object>> getVehicleByVendorId(Integer vendorId) {
        return vehicleRepo.getVehicleList(vendorId);
    }

    @Override
    public List<OwnershipResponseDto> getOwnershipRequestList() {
        List<OwnershipRequestProjection> requestProjectionList = ownershipTransferRepo.getOwnershipTransferList();
        List<OwnershipResponseDto> ownershipResponseDtoList = new ArrayList<>();
        for (OwnershipRequestProjection ownershipRequest : requestProjectionList) {
            OwnershipResponseDto ownershipResponseDto = OwnershipResponseDto.builder()
                    .id(ownershipRequest.getId())
                    .vehicleType(ownershipRequest.getVehicleType())
                    .manufactureYear(ownershipRequest.getManufactureYear())
                    .companyCode(ownershipRequest.getCompanyCode())
                    .requestDate(ownershipRequest.getRequestDate())
                    .vehicleIdentificationNo(ownershipRequest.getVehicleIdentificationNo())
                    .buyerName(ownershipRequest.getBuyerName())
                    .buyerMobileNumber(ownershipRequest.getBuyerMobileNumber())
                    .buyerEmail(ownershipRequest.getBuyerEmail())
                    .buyerAddress(ownershipRequest.getBuyerAddress())
                    .buyerProfileUrl(imageAccessBaseUrl.concat("/profile/").concat(getImageNameBuyUrl(ownershipRequest.
                            getBuyerProfileUrl())))
                    .buyerCitizenshipNo(ownershipRequest.getBuyerCitizenshipNo())
                    .buyerCitizenshipFontUrl(imageAccessBaseUrl.concat("/citizen/").concat(getImageNameBuyUrl
                            (ownershipRequest.getBuyerCitizenshipFontUrl())))
                    .buyerCitizenshipBackUrl(imageAccessBaseUrl.concat("/citizen/").concat(getImageNameBuyUrl
                            (ownershipRequest.getBuyerCitizenshipBackUrl())))
                    .sellerName(ownershipRequest.getSellerName())
                    .sellerMobileNumber(ownershipRequest.getSellerMobileNumber())
                    .sellerEmail(ownershipRequest.getSellerEmail())
                    .sellerAddress(ownershipRequest.getSellerAddress())
                    .sellerCitizenshipNo(ownershipRequest.getSellerCitizenshipNo())
                    .sellerProfileUrl(imageAccessBaseUrl.concat("/profile/").concat(getImageNameBuyUrl(ownershipRequest.
                            getSellerProfileUrl())))
                    .sellerCitizenshipFontUrl(imageAccessBaseUrl.concat("/citizen/").concat(getImageNameBuyUrl(ownershipRequest.
                            getSellerCitizenshipFontUrl())))
                    .sellerCitizenshipBackUrl(imageAccessBaseUrl.concat("/citizen/").concat(getImageNameBuyUrl(ownershipRequest.
                            getSellerCitizenshipBackUrl()))).build();
            ownershipResponseDtoList.add(ownershipResponseDto);
        }
        return ownershipResponseDtoList;
    }


    private String saveAndScanImage(MultipartFile scanImage, String languageCode) throws
            IOException, TesseractException {
        // first validate number plate image extension
        String ppExtension = imageValidation.validateImage(scanImage);

        String uploadDir = "";
        Path uploadPath = null;
        Path scanImageFilePath;
        if (scanImage != null) {
            String scanImageName = imageUtils.generateUniqueImageName(imageUtils.generateRandomString(),
                    imageUtils.generateRandomInt(), "scan_image", ppExtension);
            // create folder if not already not exists
            uploadDir = System.getProperty("user.home").concat("/vvts/scan_image");
            uploadPath = Paths.get(uploadDir);
            // create upload file directory if already not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // create full url and save image
            scanImageFilePath = uploadPath.resolve(scanImageName);
            scanImage.transferTo(scanImageFilePath);

            // now scan that save number plate image
            String scanOutput = imageScanner.scan(String.valueOf(scanImageFilePath), languageCode);
            return scanOutput;
        }
        return null;
    }

    private String getImageNameBuyUrl(String imageUrl) {
        return imageUrl.split("/")[imageUrl.split("/").length - 1];
    }
}
