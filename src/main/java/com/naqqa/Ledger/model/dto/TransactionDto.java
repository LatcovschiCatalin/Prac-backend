package com.naqqa.Ledger.model.dto;

import com.naqqa.Ledger.enums.ExpenseType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private Boolean isExpense;
    private Float amount;
    private ExpenseType type;
    private String description;
    private LocalDateTime date;
}
