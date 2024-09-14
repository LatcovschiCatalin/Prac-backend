package com.naqqa.Ledger.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Setter
@Getter
@AllArgsConstructor
@RedisHash(value = "RegisterRequest", timeToLive = 600L)
public class RegisterRequestEntity {

    @Id
    private String email;
    private String username;
    private String uuid;
    private String password;

}
