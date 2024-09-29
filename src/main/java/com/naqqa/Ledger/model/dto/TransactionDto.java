package com.naqqa.Ledger.model.dto;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private float amount;
    private LocalDateTime date;
    private TransactionType type;
}
