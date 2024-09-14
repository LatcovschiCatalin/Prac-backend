package com.naqqa.Ledger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPasswordRequest {

    private final String email;
    private final String uuid;
    private final String password;

}
