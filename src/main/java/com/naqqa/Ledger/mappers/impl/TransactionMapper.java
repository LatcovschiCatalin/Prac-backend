package com.naqqa.Ledger.mappers.impl;

import com.naqqa.Ledger.entities.TransactionEntity;
import com.naqqa.Ledger.mappers.Mapper;
import com.naqqa.Ledger.model.dto.TransactionDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionMapper implements Mapper<TransactionEntity, TransactionDto> {

    private final ModelMapper modelMapper;

    @Override
    public TransactionDto mapTo(TransactionEntity transactionEntity) {
        return modelMapper.map(transactionEntity, TransactionDto.class);
    }

    @Override
    public TransactionEntity mapFrom(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto, TransactionEntity.class);
    }
}
