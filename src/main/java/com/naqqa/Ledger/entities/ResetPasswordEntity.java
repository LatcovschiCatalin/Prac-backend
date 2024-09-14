package com.naqqa.Ledger.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Setter
@Getter
@AllArgsConstructor
@RedisHash(value = "RegisterRequest", timeToLive = 240L)
public class ResetPasswordEntity {

    @Id
    private String email;
    private String uuid;

}
