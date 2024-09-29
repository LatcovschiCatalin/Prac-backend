package com.naqqa.Ledger.repositories;

import com.naqqa.Ledger.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
