package com.vvts.service;

import com.vvts.dto.*;
import com.vvts.projection.InitProjection;
import com.vvts.projection.UserBasicProjection;
import com.vvts.projection.UserCommonDetailProjection;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

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
    List<UserKycDetailDto> getActiveUserList() throws IOException;

    List<InitProjection> getRoleModuleMappingDetail(Integer roleId);

    UserBasicProjection getUserByUserId(Integer userId);

    String getTakeActionOnKycRequest(Integer userId, String actionType);

    String getProfileImagePathOfLoginUser(Integer loginUserId);

    String getGenerateQrCode(Integer loginUserId) throws IOException;

    ResponseEntity<Resource> downloadImage(Integer loginUserId);

    NumberPlateScannerResponsePojo getUserById(Integer userId);

    UserCommonDetailDto getUserDetail(Integer userId);


}
