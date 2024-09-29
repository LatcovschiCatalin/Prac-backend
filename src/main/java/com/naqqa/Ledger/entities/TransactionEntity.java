package com.naqqa.Ledger.entities;

import com.naqqa.Ledger.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private float amount;
    private LocalDateTime date = LocalDateTime.now();

    private TransactionType type;
}
