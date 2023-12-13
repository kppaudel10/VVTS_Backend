package com.vvts.service.impl;

import com.vvts.dto.TaxClearanceDto;
import com.vvts.entity.TaxClearance;
import com.vvts.entity.Users;
import com.vvts.entity.VehicleDetail;
import com.vvts.enums.VehicleType;
import com.vvts.projection.TaxClearanceProjection;
import com.vvts.repo.TaxClearanceRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.TaxClearanceService;
import com.vvts.utiles.ImageUtils;
import com.vvts.utiles.ImageValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class TaxClearanceServiceImpl implements TaxClearanceService {

    private final UsersRepo usersRepo;

    private final VehicleRepo vehicleRepo;

    private final TaxClearanceRepo taxClearanceRepo;

    private final ImageValidation imageValidation;

    private final ImageUtils imageUtils;

    @Override
    public TaxClearanceDto saveTaxClearance(TaxClearanceDto taxClearanceDto, Integer userId) throws IOException {
        // check user details is valid or not
        Users users = usersRepo.getUsersByCitizenshipNo(taxClearanceDto.getCitizenshipNo());
        if (users == null) {
            Optional<Users> usersOptional = usersRepo.findById(userId);
            if (!usersOptional.isPresent()) {
                throw new RuntimeException("Invalid citizenship no: " + taxClearanceDto.getCitizenshipNo());
            } else {
                users = usersOptional.get();
            }
        }
        // check vehicle details is valid or not
        VehicleDetail vehicleDetail = vehicleRepo.getVehicleDetailByVehicleIdentificationNo(taxClearanceDto.getVehicleIdentificationNo());
        if (vehicleDetail == null) {
            throw new RuntimeException("Invalid vehicle identification no : " + taxClearanceDto.getVehicleIdentificationNo());
        }
        TaxClearance taxClearance = taxClearanceRepo.getTaxClearanceByVehicleId(vehicleDetail.getId());
        if (taxClearance != null && (taxClearance.getIsRejected() == null || !taxClearance.getIsRejected()) &&
                (taxClearance.getIsVerified() == null || !taxClearance.getIsVerified())) {
            throw new RuntimeException("Your tax clearance request already in pending !");
        }
        // check already verified for current year or not
        Integer validYear = taxClearanceRepo.getValidYear(vehicleDetail.getId());
        if (validYear != null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            if (year == validYear) {
                throw new RuntimeException("Your tax has been clear for the year(" + year + ").");
            }
        }
        // check paid amount validation
        checkTaxAboutValidity(taxClearanceDto.getPaidAmount(), vehicleDetail);
        // save the tax clearance bill doc
        // validate image
        String extension = imageValidation.validateImage(taxClearanceDto.getAmountPaidSheet());
        // get name
        String profilePictureName = imageUtils.generateUniqueImageName(users.getName(), users.getId(), "taxClearance", extension);
        String uploadDir = "";
        Path uploadPath = null;
        Path taxClearanceImagePath = null;

        if (taxClearanceDto.getAmountPaidSheet() != null) {
            // create folder if not already not exists
            uploadDir = System.getProperty("user.home").concat("/vvts/tax-clearance");
            uploadPath = Paths.get(uploadDir);
            // create upload file directory if already not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // create full url
            taxClearanceImagePath = uploadPath.resolve(profilePictureName);
            taxClearanceDto.getAmountPaidSheet().transferTo(taxClearanceImagePath);
        }

        // save tax clearance
        taxClearance = TaxClearance.builder()
                .id(taxClearanceDto.getId())
                .paidUser(users)
                .isVerified(false)
                .isRejected(false)
                .processDate(new Date())
                .vehicleDetail(vehicleDetail)
                .paidAmount(taxClearanceDto.getPaidAmount())
                .paidBillDocUrl(String.valueOf(taxClearanceImagePath))
                .build();
        taxClearanceRepo.save(taxClearance);
        taxClearanceDto.setAmountPaidSheet(null);
        return taxClearanceDto;
    }

    @Override
    public List<TaxClearanceProjection> getTaxClearanceListByUserId(Integer loginUserId, Boolean isAll) {
        if (isAll != null && isAll) {
            return taxClearanceRepo.getTaxClearanceRequestForAdmin();
        }
        return taxClearanceRepo.getTaxClearanceByLoginUserId(loginUserId);
    }

    @Transactional
    @Override
    public String actionTaxClearanceRequest(String action, Integer taxClearanceId) {
        if (action != null) {
            if (action.equalsIgnoreCase("Accept")) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                taxClearanceRepo.acceptTaxClearanceRequest(taxClearanceId, new Date(), String.valueOf(year));
                return "Tax Clearance Request accepted successfully !";
            }
            if (action.equalsIgnoreCase("Reject")) {
                taxClearanceRepo.rejectTaxClearanceRequest(taxClearanceId);
                return "Tax Clearance request rejected successfully !";
            }
        }
        return null;
    }


    private void checkTaxAboutValidity(Double amount, VehicleDetail vehicleDetail) {
        if (vehicleDetail.getVehicleType().equals(VehicleType.SCOOTER)
                || vehicleDetail.getVehicleType().equals(VehicleType.BIKE)) {
            Double amountNeedToPaid = getBikeScooterTaxAmount(vehicleDetail.getCc());
            if (!amount.equals(amountNeedToPaid)) {
                throw new RuntimeException("You should have paid Rs: " + amountNeedToPaid);
            }
        } else {
            Double amountNeedToPaid = getCarTaxAmount(vehicleDetail.getCc());
            if (amount != amountNeedToPaid) {
                throw new RuntimeException("You should have paid Rs: " + amountNeedToPaid);
            }
        }
    }

    private Double getBikeScooterTaxAmount(Integer cc) {
        if (cc < 125) {
            return 3000D;
        } else if (cc < 150) {
            return 5000D;
        } else if (cc < 225) {
            return 6500D;
        } else if (cc < 400) {
            return 11000D;
        } else if (cc < 650) {
            return 20000D;
        } else {
            return 30000D;
        }
    }

    private Double getCarTaxAmount(Integer cc) {
        if (cc < 1000) {
            return 22000D;
        } else if (cc < 1500) {
            return 25000D;
        } else if (cc < 225) {
            return 6500D;
        } else if (cc < 400) {
            return 11000D;
        } else if (cc < 650) {
            return 20000D;
        } else {
            return 30000D;
        }
    }

    private String getFileNameFormPath(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        return fileName;
    }

}
