package com.naqqa.Ledger.repositories;

import com.naqqa.Ledger.entities.ResetPasswordEntity;
import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordRepository extends CrudRepository<ResetPasswordEntity, String> {
}
