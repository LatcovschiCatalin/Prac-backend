package com.naqqa.Ledger.model.dto;

import com.naqqa.Ledger.enums.AuthMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private AuthMethod authMethod;
}
