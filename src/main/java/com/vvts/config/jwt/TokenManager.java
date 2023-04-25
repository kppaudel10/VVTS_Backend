package com.vvts.config.jwt;

import com.vvts.entity.Users;
import com.vvts.repo.UsersRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenManager implements Serializable {
    public static final long TOKEN_VALIDITY = 100 * 60 * 60;

    private static final long serialVersionUID = 7008375124389347049L;

    private final UsersRepo usersRepo;

    @Value("${secret}")
    private String jwtSecret;

    public String generateJwtToken(UserDetails userDetails) {
        // get uses details using username
        Users users = usersRepo.getUsersByMobileNumber(userDetails.getUsername());
        if (users == null) {
            throw new RuntimeException("Username: " + userDetails.getUsername() + " does not exists");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", users.getId());
        claims.put("name", users.getName());
        claims.put("username", userDetails.getUsername());
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getUserFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }

    public String getUserFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
