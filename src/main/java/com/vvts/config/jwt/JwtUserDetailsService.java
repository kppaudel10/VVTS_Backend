package com.vvts.config.jwt;

import com.vvts.entity.AdministrativeUser;
import com.vvts.entity.PublicUser;
import com.vvts.repo.AdministrativeUserRepo;
import com.vvts.repo.PublicUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final PublicUserRepo publicUserRepo;
    private final AdministrativeUserRepo administrativeUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdministrativeUser administrativeUser = administrativeUserRepo.getAdministrativeUserByUserName(username);
        if (administrativeUser == null) {
            PublicUser publicUser = publicUserRepo.getPublicUserByMobileNumber(username);
            if (publicUser == null){
                throw new UsernameNotFoundException("User not found with username: " + username);
            }else {
                return new User(username,
                        publicUser.getPassword(),
                        new ArrayList<>());
            }
        }else {
            return new User(username,
                    administrativeUser.getPassword(),
                    new ArrayList<>());

        }

    }
}
