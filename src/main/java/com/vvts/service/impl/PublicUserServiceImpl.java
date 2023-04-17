package com.vvts.service.impl;

import com.vvts.dto.PublicUserDto;
import com.vvts.entity.PublicUser;
import com.vvts.repo.PublicUserRepo;
import com.vvts.service.PublicUserService;
import com.vvts.utiles.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
@Service
@RequiredArgsConstructor
public class PublicUserServiceImpl implements PublicUserService {

    private final PublicUserRepo publicUserRepo;

    @Override
    public PublicUserDto savePublicUser(PublicUserDto publicUserDto) {
        // build entity
        PublicUser publicUser = PublicUser.builder()
                .id(publicUserDto.getId())
                .name(publicUserDto.getName())
                .address(publicUserDto.getAddress())
                .email(publicUserDto.getEmail())
                .mobileNumber(publicUserDto.getMobileNumber())
                .isEnable(false)
                .password(new PasswordEncoder().getEncryptedPassword(publicUserDto.getPassword()))
                .build();
       publicUser = publicUserRepo.save(publicUser);
        publicUserDto.setId(publicUser.getId());
        return publicUserDto;
    }
}
