package com.naqqa.Ledger.controllers;

import com.naqqa.Ledger.entities.TransactionEntity;
import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.model.dto.TransactionDto;
import com.naqqa.Ledger.repositories.UserRepository;
import com.naqqa.Ledger.services.AuthenticationService;
import com.naqqa.Ledger.services.LedgerService;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LedgerController {
    private final LedgerService ledgerService;

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping("/transactions")
    public ResponseEntity<String> createTransaction(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody TransactionEntity transaction) {

        Optional<UserEntity> user = authenticationService.authorizeUser(token);

        if (user.isEmpty())
            return new ResponseEntity<>("No such user found", HttpStatus.FORBIDDEN);

        ledgerService.createTransaction(transaction, user.get());

        return new ResponseEntity<>("Transaction created successful", HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        Optional<UserEntity> user = authenticationService.authorizeUser(token);

        return user.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(ledgerService.getUserTransactions(user.get()), HttpStatus.CREATED);
    }
}
