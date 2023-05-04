package com.vvts.service.impl;

import com.vvts.dto.BuyRequestPojo;
import com.vvts.dto.MailSendDto;
import com.vvts.dto.NumberPlateScannerResponsePojo;
import com.vvts.dto.VehicleDto;
import com.vvts.entity.OwnershipTransfer;
import com.vvts.entity.Users;
import com.vvts.entity.VehicleDetail;
import com.vvts.enums.VehicleType;
import com.vvts.projection.BuyRequestProjection;
import com.vvts.projection.NumberPlateScannerProjection;
import com.vvts.repo.OwnershipTransferRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.VehicleService;
import com.vvts.utiles.ImageScanner;
import com.vvts.utiles.ImageUtils;
import com.vvts.utiles.ImageValidation;
import com.vvts.utiles.VINGenerator;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        MailSendDto mailSendDto = new MailSendDto();
        mailSendDto.setEmail(users.getEmail());
        mailSendDto.setUserName(users.getName());
        mailSendDto.setMessage("Your pin code");
//        new MailSend().sendMail(mailSendDto);
        // build entity
        Users buyer = new Users();
        buyer.setId(loginUserId);
        OwnershipTransfer ownershipTransfer = OwnershipTransfer.builder()
                .transferRequestDate(new Date())
                .vehicleIdentificationNo(buyRequestPojo.getVehicleIdentificationNo())
                .seller(users)
                .buyer(buyer).build();
        ownershipTransferRepo.save(ownershipTransfer);
        return buyRequestPojo;
    }

    @Override
    public List<BuyRequestProjection> getBuyRequestList(Integer loginUserId) {
        return ownershipTransferRepo.getOwnershipTransferByOwnerId(loginUserId);
    }

    @Override
    public NumberPlateScannerResponsePojo getScanNumberPlate(MultipartFile scanImage) throws IOException, TesseractException {
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
            String scanOutput = imageScanner.scan(String.valueOf(scanImageFilePath));
            if (scanOutput != null) {
                NumberPlateScannerProjection npcp = vehicleRepo.getUserAndVehicleDetailByNumberPlate(scanOutput);
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
        }
        return null;
    }
}
