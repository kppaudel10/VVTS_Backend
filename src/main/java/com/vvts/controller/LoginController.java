package com.vvts.controller;

import com.vvts.config.jwt.*;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class LoginController {

    private final JwtUserDetailsService jwtUserDetailsService;

    private final TokenManager tokenManager;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public GlobalApiResponse login(@RequestBody JwtRequestModel requestModel) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestModel.getUsername(),
                            requestModel.getPassword())
            );
        } catch (DisabledException disabledException) {
            throw new Exception("USER_DISABLED", disabledException);
        } catch (BadCredentialsException badCredentialsException) {
            throw new Exception("INVALID_CREDENTIALS", badCredentialsException);
        }
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(requestModel.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        return new GlobalApiResponse("token", true, new JwtResponseModel(jwtToken));
    }

}
