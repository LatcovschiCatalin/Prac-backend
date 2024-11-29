package com.naqqa.Ledger.controllers;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.enums.ExpenseType;
import com.naqqa.Ledger.model.dto.TransactionDto;
import com.naqqa.Ledger.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<String> postTransaction(@AuthenticationPrincipal UserEntity user,
                                                  @RequestBody TransactionDto transaction) {
        transactionService.save(transaction, user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping()
    public ResponseEntity<String> putTransaction(@AuthenticationPrincipal UserEntity user,
                                                 @RequestBody TransactionDto transactionDto) {
        transactionService.patchTransaction(user.getId(), transactionDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<TransactionDto>> getTransactions(@AuthenticationPrincipal UserEntity user,
                                                                @PageableDefault(size = 20) Pageable pageable,
                                                                @RequestParam(name = "type", required = false) List<Long> types) {
        return new ResponseEntity<>(transactionService.findAllByType(user.getId(), pageable, types), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@AuthenticationPrincipal UserEntity user,
                                                    @PathVariable("id") Long id) {
        transactionService.deleteById(id, user.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getTransactionTypes() {
        List<String> types = new ArrayList<>();

        for (ExpenseType type : ExpenseType.values())
            types.add(type.name());

        return new ResponseEntity<>(types, HttpStatus.OK);
    }
}
