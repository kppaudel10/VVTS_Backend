package com.vvts.service.impl;

import com.vvts.config.jwt.JwtUserDetailsService;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.entity.Role;
import com.vvts.entity.Users;
import com.vvts.repo.AccessTokenRepo;
import com.vvts.repo.RoleRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepo usersRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final AccessTokenRepo accessTokenRepo;


    @Override
    public PublicUserBasicDataDto savePublicUser(PublicUserBasicDataDto publicUserBasicDataDto) {
        if (publicUserBasicDataDto.getId() == null) {
            // check mobile number or email already exits or not
            if (usersRepo.getMobileNumberCount(publicUserBasicDataDto.getMobileNumber()) > 0) {
                throw new RuntimeException("Mobile Number: " + publicUserBasicDataDto.getMobileNumber() + " already exists");
            }
            if (usersRepo.getEmailCount(publicUserBasicDataDto.getEmail()) > 0) {
                throw new RuntimeException("Email Address : " + publicUserBasicDataDto.getEmail() + " already exists");
            }
        } else {
            // check mobile number or email already exits or not
            if (usersRepo.getMobileNumberCount(publicUserBasicDataDto.getMobileNumber()) > 1) {
                throw new RuntimeException("Mobile Number: " + publicUserBasicDataDto.getMobileNumber() + " already exists");
            }
            if (usersRepo.getEmailCount(publicUserBasicDataDto.getEmail()) > 1) {
                throw new RuntimeException("Email Address : " + publicUserBasicDataDto.getEmail() + " already exists");
            }
        }
        // get user role
        Role role = roleRepo.getRoleByRoleName(publicUserBasicDataDto.getRoleName());
        if (role == null) {
            throw new RuntimeException("Invalid Role name : " + publicUserBasicDataDto.getRoleName() + ".");
        }
        // build entity
        Users users = Users.builder()
                .id(publicUserBasicDataDto.getId())
                .name(publicUserBasicDataDto.getName())
                .email(publicUserBasicDataDto.getEmail())
                .mobileNumber(publicUserBasicDataDto.getMobileNumber())
                .role(role)
                .isEnable(false)
                .password(passwordEncoder.encode(publicUserBasicDataDto.getPassword()))
                .build();
        usersRepo.save(users);
        publicUserBasicDataDto.setId(users.getId());

        return publicUserBasicDataDto;
    }

    @Override
    @Transactional
    public Boolean logoutUser() {
        // get username of login user
        try {
            User user = jwtUserDetailsService.getAuthenticatedUser();
            accessTokenRepo.deleteAccessTokenByUserName(user.getUsername());
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
