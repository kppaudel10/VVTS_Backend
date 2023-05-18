package com.vvts.config.jwt;

import com.vvts.entity.AccessToken;
import com.vvts.repo.AccessTokenRepo;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final JwtUserDetailsService jwtUserDetailsService;

    private final AccessTokenRepo accessTokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, ServletException {

        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
        } else {
            token = tokenHeader;
        }
        if (token != null) {
            try {
                username = tokenManager.getUserFromToken(token);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                throw new RuntimeException("JWT Token has expired");
            }
        }

        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (token != null && tokenManager.validateJwtToken(token, userDetails) && isAccessTokenExists(username, token)) {
                UsernamePasswordAuthenticationToken
                        authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                accessTokenRepo.deleteAccessTokenByUserName(username);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isAccessTokenExists(String username, String token) {
        AccessToken accessToken = accessTokenRepo.getAccessTokenExistsOrNot(username);
        if (accessToken != null && token.equals(accessToken.getAccessToken())) {
            return true;
        } else {
            return false;
        }
    }
}
