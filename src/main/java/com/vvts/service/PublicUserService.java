package com.vvts.service;

import com.vvts.dto.PublicUserBasicDataDto;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

public interface PublicUserService {

    PublicUserBasicDataDto savePublicUser(PublicUserBasicDataDto publicUserBasicDataDto) throws IOException;

}
