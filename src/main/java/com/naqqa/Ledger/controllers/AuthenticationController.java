package com.naqqa.Ledger.controllers;

import com.naqqa.Ledger.model.*;
import com.naqqa.Ledger.services.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (authenticationService.isRegistered(request.email()))
            return new ResponseEntity<>("This email address is already registered!", HttpStatus.CONFLICT);

        authenticationService.register(request);

        return new ResponseEntity<>("A confirmation link has been sent on email!", HttpStatus.CREATED);
    }

//    @PostMapping("/register-social-login")
//    public ResponseEntity<AuthenticationResponse> registerSocialLogin(@RequestBody SocialLoginRequest request) {
//        SocialLoginTokenInfo tokenInfo = authenticationService.validateSocialLoginRequest(request);
//
//        if (tokenInfo == null)
//            return new ResponseEntity<>(new AuthenticationResponse(null, "Invalid token!"), HttpStatus.FORBIDDEN);
//
//        if (authenticationService.isRegistered(tokenInfo, request.getAuthMethod()))
//            return new ResponseEntity<>(new AuthenticationResponse(null, "This email address is already registered!"), HttpStatus.CONFLICT);
//
//        authenticationService.register(tokenInfo, request.getAuthMethod());
//
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @PostMapping("/register-confirm")
    public ResponseEntity<AuthenticationResponse> registerConfirm(@RequestBody RegisterConfirmRequest request) {

        if (!authenticationService.isRegisterConfirmRequestValid(request))
            return new ResponseEntity<>(AuthenticationResponse.builder().message("Invalid request").build(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(authenticationService.registerConfirm(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        if (!authenticationService.isRegistered(request.getEmail()))
            return new ResponseEntity<>(AuthenticationResponse.builder().message("Account does not exist!").build(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }

    @PostMapping("/social-login")
    public ResponseEntity<AuthenticationResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        SocialLoginTokenInfo tokenInfo = authenticationService.validateSocialLoginRequest(request);

        if (tokenInfo == null)
            return new ResponseEntity<>(AuthenticationResponse.builder().message("Invalid token!").build(), HttpStatus.FORBIDDEN);

        AuthenticationResponse response;
        if (authenticationService.isRegistered(tokenInfo, request.getAuthMethod()))
            response = authenticationService.login(tokenInfo, request.getAuthMethod());
        else
            response = authenticationService.register(tokenInfo, request.getAuthMethod());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (!authenticationService.isRegistered(request.email()))
            return new ResponseEntity<>("Account does not exist!", HttpStatus.NOT_FOUND);

        authenticationService.forgetPassword(request);
        return new ResponseEntity<>("Password reset link sent on email!", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthenticationResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (!authenticationService.isRegistered(request.getEmail()))
            return new ResponseEntity<>(AuthenticationResponse.builder().message("Account does not exist!").build(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(authenticationService.resetPassword(request), HttpStatus.OK);
    }

}
