package com.naqqa.Ledger.entities;

import com.naqqa.Ledger.enums.ExpenseType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    private Boolean isExpense;
    private float amount;
    private ExpenseType type;

    private String description;

    private LocalDateTime date;

    @PrePersist
    private void prePersist() {
        date = LocalDateTime.now();
    }
}
