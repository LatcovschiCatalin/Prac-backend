package com.naqqa.Ledger.controllers;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.model.UpdateUserRequest;
import com.naqqa.Ledger.model.dto.UserDto;
import com.naqqa.Ledger.services.AuthenticationService;
import com.naqqa.Ledger.services.UserService;
import com.sendgrid.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    AuthenticationService authenticationService;
    UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<String> updateUserDetails(@AuthenticationPrincipal UserEntity user,
                                                    @RequestBody UpdateUserRequest request) {
        userService.updateUserDetails(user, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserDetails(@AuthenticationPrincipal UserEntity user) {
        return new ResponseEntity<>(userService.getCurrentUserDetails(user), HttpStatus.OK);
    }

}
