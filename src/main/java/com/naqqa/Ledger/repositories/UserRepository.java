package com.naqqa.Ledger.repositories;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.enums.AuthMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String email);
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    @Query(value = "SELECT u.* " +
            "  FROM users as u " +
            "WHERE u.email = :email AND u.auth_method = :auth_method", nativeQuery = true)
    Optional<UserEntity> findByEmailAndAuthMethod(@Param("email") String email, @Param("auth_method") AuthMethod authMethod);
}
