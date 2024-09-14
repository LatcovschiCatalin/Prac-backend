package com.naqqa.Ledger.repositories;

import com.naqqa.Ledger.entities.RegisterRequestEntity;
import org.springframework.data.repository.CrudRepository;

public interface RegisterRequestRepository extends CrudRepository<RegisterRequestEntity, String> {
}
