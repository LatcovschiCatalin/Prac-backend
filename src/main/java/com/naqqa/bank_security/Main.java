package com.naqqa.bank_security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Main {

    @GetMapping("/")
    public String home() {
        return "<h1> Home </h1>";
    }

}
