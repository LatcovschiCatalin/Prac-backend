package com.naqqa.Ledger.model;

public record RegisterRequest (

    String username,
    String email,
    String password

) {}
