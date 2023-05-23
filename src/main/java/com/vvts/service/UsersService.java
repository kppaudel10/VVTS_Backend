package com.vvts.service;

import com.vvts.dto.KycUpdateResponseDto;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.dto.UserKycDetailDto;
import com.vvts.dto.UserKycUpdateDto;
import com.vvts.projection.InitProjection;
import com.vvts.projection.UserBasicProjection;

import java.io.IOException;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

public interface UsersService {

    PublicUserBasicDataDto savePublicUser(PublicUserBasicDataDto publicUserBasicDataDto) throws IOException;

    Boolean logoutUser(String userName);

    KycUpdateResponseDto updateUserKyc(UserKycUpdateDto userKycUpdateDto) throws IOException;

    List<UserKycDetailDto> getNewKycRequest() throws IOException;

    List<InitProjection> getRoleModuleMappingDetail(Integer roleId);

    UserBasicProjection getUserByUserId(Integer userId);

    String getTakeActionOnKycRequest(Integer userId, String actionType);

    String getProfileImagePathOfLoginUser(Integer loginUserId);

}
