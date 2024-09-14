package com.naqqa.Ledger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterConfirmRequest {

    private final String email;
    private final String uuid;

}
