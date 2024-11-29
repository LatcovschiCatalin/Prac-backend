package com.naqqa.Ledger.services;

import com.naqqa.Ledger.entities.TransactionEntity;
import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.mappers.impl.TransactionMapper;
import com.naqqa.Ledger.model.dto.TransactionDto;
import com.naqqa.Ledger.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public void save(TransactionDto transaction, UserEntity user) {
        TransactionEntity transactionEntity = transactionMapper.mapFrom(transaction);

        transactionEntity.setUser(user);

        transactionRepository.save(transactionEntity);
    }

    public Page<TransactionDto> findAllByType(Long userId, Pageable pageable, List<Long> types) {
        Page<TransactionEntity> transactionEntities;

        if (types == null)
            transactionEntities = transactionRepository.findAllByUser(userId, pageable);
        else
            transactionEntities = transactionRepository.findAllByTypes(userId, pageable, types);

        return transactionEntities.map(transactionMapper::mapTo);
    }

    public void deleteById(Long id, Long userId) {
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                        .orElseThrow();

        if (!Objects.equals(transactionEntity.getUser().getId(), userId))
            return;

        transactionRepository.deleteById(id);
    }

    public void patchTransaction(Long userId, TransactionDto transactionDto) {
        TransactionEntity transactionEntity = transactionRepository.findById(transactionDto.getId())
                        .orElseThrow();

        transactionMapper.mapSrcToDst(transactionDto, transactionEntity);

        transactionRepository.save(transactionEntity);
    }
}
