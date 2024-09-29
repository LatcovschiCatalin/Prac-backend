package com.naqqa.Ledger.controllers;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.services.AuthenticationService;
import com.naqqa.Ledger.services.UserService;
import com.sendgrid.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/me")
    public ResponseEntity<String> updateUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserEntity updatedUser) {
        Optional<UserEntity> user = authenticationService.authorizeUser(token);

        if (user.isEmpty())
            return new ResponseEntity<>("No such user found", HttpStatus.FORBIDDEN);

        userService.updateUserDetails(user.get(), updatedUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
