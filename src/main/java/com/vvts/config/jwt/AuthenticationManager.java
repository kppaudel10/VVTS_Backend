package com.vvts.config.jwt;

import com.vvts.entity.AdministrativeUser;
import com.vvts.entity.PublicUser;
import com.vvts.repo.AdministrativeUserRepo;
import com.vvts.repo.PublicUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    private final PublicUserRepo publicUserRepo;

    private final AdministrativeUserRepo administrativeUserRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        AdministrativeUser administrativeUser = administrativeUserRepo.getAdministrativeUserByUserName(username);
        if (administrativeUser == null) {
            PublicUser publicUser = publicUserRepo.getPublicUserByMobileNumber(username);
            if (publicUser == null){
                throw new BadCredentialsException("Error!!");
            }
            grantedAuths.add(new SimpleGrantedAuthority("Public User"));
            if (passwordEncoder.matches(password, publicUser.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
            }
        }else {
            grantedAuths.add(new SimpleGrantedAuthority(administrativeUser.getRole().getRoleName()));
            if (passwordEncoder.matches(password, administrativeUser.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
            }
        }
        return null;
    }
}
