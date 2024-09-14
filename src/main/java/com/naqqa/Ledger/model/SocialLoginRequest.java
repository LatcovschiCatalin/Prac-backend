package com.naqqa.Ledger.model;


import com.naqqa.Ledger.enums.AuthMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocialLoginRequest {

    private final String token;
    private final AuthMethod authMethod;

}
