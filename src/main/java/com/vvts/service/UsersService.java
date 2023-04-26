package com.vvts.service;

import com.vvts.dto.KycUpdateResponseDto;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.dto.UserKycUpdateDto;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

public interface UsersService {

    PublicUserBasicDataDto savePublicUser(PublicUserBasicDataDto publicUserBasicDataDto) throws IOException;
    Boolean logoutUser();
    KycUpdateResponseDto updateUserKyc(UserKycUpdateDto userKycUpdateDto) throws IOException;

}
