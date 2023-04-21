package com.vvts.service.impl;

import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.entity.PublicUser;
import com.vvts.repo.PublicUserRepo;
import com.vvts.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
@Service
@RequiredArgsConstructor
public class PublicUserServiceImpl implements PublicUserService {

    private final PublicUserRepo publicUserRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public PublicUserBasicDataDto savePublicUser(PublicUserBasicDataDto publicUserBasicDataDto) throws IOException {
        // build entity
        PublicUser publicUser = PublicUser.builder()
                .id(publicUserBasicDataDto.getId())
                .name(publicUserBasicDataDto.getName())
                .email(publicUserBasicDataDto.getEmail())
                .mobileNumber(publicUserBasicDataDto.getMobileNumber())
                .isEnable(false)
                .password(bCryptPasswordEncoder.encode(publicUserBasicDataDto.getPassword()))
                .build();
        publicUserRepo.save(publicUser);
        publicUserBasicDataDto.setId(publicUser.getId());

        return publicUserBasicDataDto;
    }

}
