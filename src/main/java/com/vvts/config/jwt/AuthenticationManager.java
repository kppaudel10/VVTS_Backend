package com.vvts.config.jwt;

import com.vvts.entity.Users;
import com.vvts.repo.UsersRepo;
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

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        Users users = usersRepo.getUsersByMobileNumber(username);
        if (users == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        grantedAuths.add(new SimpleGrantedAuthority(users.getRole().getRoleName()));
        if (passwordEncoder.matches(password, users.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            throw new BadCredentialsException("Bad Credentials");
        }
    }
}
