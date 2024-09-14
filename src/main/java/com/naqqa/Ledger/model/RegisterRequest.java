package com.naqqa.Ledger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@Getter
public record RegisterRequest (

    String username,
    String email,
    String password

) {}
