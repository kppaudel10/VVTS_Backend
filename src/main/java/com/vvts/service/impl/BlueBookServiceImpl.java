package com.vvts.service.impl;

import com.vvts.dto.BlueBookDto;
import com.vvts.dto.VehicleTypePojo;
import com.vvts.entity.BlueBook;
import com.vvts.enums.VehicleType;
import com.vvts.repo.BlueBookRepo;
import com.vvts.service.BlueBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Service
@RequiredArgsConstructor
public class BlueBookServiceImpl implements BlueBookService {

    private final BlueBookRepo blueBookRepo;

    @Override
    public List<VehicleTypePojo> getAllVehicleType() {
        return VehicleType.getVehicleTypeList();
    }

    @Override
    public BlueBookDto saveBlueBook(BlueBookDto blueBookDto) {
        BlueBook blueBook = BlueBook.builder()
                .id(blueBookDto.getId())
                .citizenshipNo(blueBookDto.getCitizenshipNo())
                .effectiveDate(new Date())
                .vehicleType(VehicleType.getVehicleTypeKey(blueBookDto.getVehicleType()))
                .vehicleIdentificationNo(VINGenerator.generateVIN())
                .build();
        //save
       blueBook = blueBookRepo.save(blueBook);
       blueBookDto.setId(blueBook.getId());
       blueBookDto.setVehicleIdentificationNo(blueBook.getVehicleIdentificationNo());
        return blueBookDto;
    }

}

class VINGenerator {
    public static String generateVIN() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // First 3 characters represent the manufacturer identifier
        sb.append((char) (random.nextInt(26) + 'A')); // First character
        sb.append((char) (random.nextInt(26) + 'A')); // Second character
        sb.append((char) (random.nextInt(26) + 'A')); // Third character

        // Next 5 characters represent the vehicle attributes
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                sb.append(random.nextInt(10)); // First character is a digit
            } else {
                sb.append((char) (random.nextInt(26) + 'A')); // Other characters are letters
            }
        }

        // Next character is the check digit, which is calculated using a formula
        sb.append(calculateCheckDigit(sb.toString()));

        // Next 6 characters represent the vehicle attributes
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // Characters are digits
        }

        return sb.toString();
    }

    public static char calculateCheckDigit(String vin) {
        // Check digit formula weights
        int[] weights = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};

        // Calculate weighted sum of characters
        int sum = 0;
        for (int i = 0; i < vin.length(); i++) {
            char c = vin.charAt(i);
            if (Character.isDigit(c)) {
                sum += (c - '0') * weights[i];
            } else if (Character.isLetter(c)) {
                sum += (c - 'A' + 1) * weights[i];
            }
        }

        // Calculate check digit as modulus 11 of weighted sum
        int remainder = sum % 11;
        if (remainder == 10) {
            return 'X';
        } else {
            return (char) (remainder + '0');
        }
    }
}

