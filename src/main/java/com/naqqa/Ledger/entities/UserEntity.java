package com.naqqa.Ledger.entities;

import com.naqqa.Ledger.enums.AuthMethod;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private int id;
    private String username;
    private String email;
    private String password;
    private AuthMethod authMethod;
    private String externalId;

    @OneToMany(mappedBy = "user")
    Set<TransactionEntity> transactions;
}
