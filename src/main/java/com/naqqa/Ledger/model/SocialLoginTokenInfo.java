package com.naqqa.Ledger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocialLoginTokenInfo {

    private final String externalId;
    private final String name;
    private final String email;

}
