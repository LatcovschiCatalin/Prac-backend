package com.naqqa.Ledger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@Getter
//@RequiredArgsConstructor
//public class ForgotPasswordRequest {
//
//    private String email;
//
//}

public record ForgotPasswordRequest (
        String email
) {}
