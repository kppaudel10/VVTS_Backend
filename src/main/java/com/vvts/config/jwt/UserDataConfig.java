package com.vvts.config.jwt;

import com.vvts.entity.Users;
import com.vvts.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Component
@RequiredArgsConstructor
public class UserDataConfig {

    private final UsersRepo usersRepo;

    public Integer getLoggedInUserId(Authentication authentication) {
        Users userDetail = getUserByUserName(authentication);
        return userDetail.getId();
    }

    public String getRole(Authentication authentication) {
        Users userDetail = getUserByUserName(authentication);
        return userDetail.getRole().getRoleName();
    }

    public Integer getRoleId(Authentication authentication) {
        Users userDetail = getUserByUserName(authentication);
        return userDetail.getRole().getId();
    }

    private Users getUserByUserName(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Users userDetail = usersRepo.getUsersByMobileNumber(user.getUsername());
        if (userDetail == null) {
            throw new RuntimeException("userName: " + user.getUsername() + " does not exits");
        }
        return userDetail;
    }

}
