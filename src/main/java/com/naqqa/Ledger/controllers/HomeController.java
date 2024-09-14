package com.naqqa.Ledger.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return new ResponseEntity<>("Public", HttpStatus.OK);
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateEndpoint() {
        return new ResponseEntity<>("Private", HttpStatus.OK);
    }

}
