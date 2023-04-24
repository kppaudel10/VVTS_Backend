package com.vvts.config.jwt;

import com.vvts.entity.Users;
import com.vvts.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepo.getUsersByMobileNumber(username);
        if (users == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority(users.getRole().getRoleName()));
            return new User(username,
                    users.getPassword(),
                    grantedAuths);
        }
    }

    public User getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return this.getByUsername(user.getUsername());
        } else {
            throw new UsernameNotFoundException(
                    "User is not authenticated; Found " + " of type " + authentication.getPrincipal()
                            .getClass() + "; Expected type User");
        }
    }

    public User getByUsername(String username) {
        Users users = usersRepo.getUsersByMobileNumber(username);
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(users.getRole().getRoleName()));
        User user = new User(users.getMobileNumber(), users.getPassword(), grantedAuths);
        return user;
    }
}

