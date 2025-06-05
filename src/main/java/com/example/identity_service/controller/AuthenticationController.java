package com.example.identity_service.controller;

import com.example.identity_service.dto.UserResponse.AuthenticationResponse;
import com.example.identity_service.dto.UserResponse.IntrospectResponse;
import com.example.identity_service.dto.request.*;
import com.example.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest authrequest){
        AuthenticationResponse result = authService.isAuth(authrequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")//verify token
    ApiResponse<IntrospectResponse> authenticate (@RequestBody IntrospectRequest authrequest)
            throws ParseException, JOSEException {
        var result = authService.confirm(authrequest);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .code(2000)
                .build();
    }

    @PostMapping("/logout")//verify token
    ApiResponse<Void> authenticate (@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {

       authService.Logout(request);

        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")//verify token
    ApiResponse<AuthenticationResponse> refresh (@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {

        return ApiResponse.<AuthenticationResponse>builder()
                .result(authService.refreshToken(request))
                .build();
    }

}
