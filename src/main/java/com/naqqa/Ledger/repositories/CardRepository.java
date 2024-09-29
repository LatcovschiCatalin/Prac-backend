package com.naqqa.Ledger.repositories;

import com.naqqa.Ledger.entities.CardEntity;
import com.naqqa.Ledger.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
}

