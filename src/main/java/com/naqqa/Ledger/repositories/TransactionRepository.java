package com.naqqa.Ledger.repositories;

import com.naqqa.Ledger.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "FROM TransactionEntity t WHERE t.user.id = :id")
    Page<TransactionEntity> findAllByUser(@Param("id") Long id, Pageable pageable);

    @Query(value = "FROM TransactionEntity t WHERE t.user.id = :id AND t.type IN :types")
    Page<TransactionEntity> findAllByTypes(@Param("id") Long userId, Pageable pageable, @Param("types") List<Long> types);
}
