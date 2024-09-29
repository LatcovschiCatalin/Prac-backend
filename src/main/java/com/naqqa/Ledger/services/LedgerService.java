package com.naqqa.Ledger.services;

import com.naqqa.Ledger.entities.TransactionEntity;
import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.mappers.impl.TransactionMapper;
import com.naqqa.Ledger.model.dto.TransactionDto;
import com.naqqa.Ledger.repositories.CardRepository;
import com.naqqa.Ledger.repositories.TransactionRepository;
import com.naqqa.Ledger.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LedgerService {
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private final TransactionMapper transactionMapper;

    public void createTransaction(TransactionEntity transaction, UserEntity user) {
        transaction.setUser(user);

        transactionRepository.save(transaction);
    }

    public List<TransactionDto> getUserTransactions(UserEntity userEntity) {
        return userEntity.getTransactions().stream().map(transactionMapper::mapTo).toList();
    }
}
