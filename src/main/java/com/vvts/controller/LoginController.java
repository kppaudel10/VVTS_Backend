package com.vvts.controller;

import com.vvts.config.jwt.*;
import com.vvts.entity.AccessToken;
import com.vvts.repo.AccessTokenRepo;
import com.vvts.service.UsersService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    private final AccessTokenRepo accessTokenRepo;

    private final UsersService usersService;

    private final MessageSource messageSource;

    private final UserDataConfig userDataConfig;

    @PostMapping("/login")
    public GlobalApiResponse login(@RequestBody JwtRequestModel requestModel) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestModel.getUsername(),
                            requestModel.getPassword())
            );
        } catch (DisabledException disabledException) {
            throw new RuntimeException("USER_DISABLED");
//            return new GlobalApiResponse(disabledException.getMessage(), false, requestModel);
        } catch (BadCredentialsException badCredentialsException) {
            throw new RuntimeException(badCredentialsException.getMessage());
//            return new GlobalApiResponse(badCredentialsException.getMessage(), false, requestModel);
        }
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(requestModel.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        // save access token into database
        AccessToken accessToken = accessTokenRepo.getAccessTokenExistsOrNot(userDetails.getUsername());
        if (accessToken == null) {
            accessToken = new AccessToken();
        }
        accessToken.setUserName(userDetails.getUsername());
        accessToken.setAccessToken(jwtToken);
        accessTokenRepo.save(accessToken);
        return new GlobalApiResponse("token", true, new JwtResponseModel(jwtToken));
    }

    @PostMapping("/api/logout")
    public GlobalApiResponse userLogout(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("user.logout", null, null), true,
                usersService.logoutUser(userDataConfig.getUserName(authentication)));
    }

    @GetMapping("/init")
    public GlobalApiResponse getUserRoleModuleDetail(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getRoleModuleMappingDetail(userDataConfig.getRoleId(authentication)));
    }


}
