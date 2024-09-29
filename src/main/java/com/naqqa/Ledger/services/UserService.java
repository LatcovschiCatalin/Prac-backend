package com.naqqa.Ledger.services;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void updateUserDetails(UserEntity user, UserEntity updatedUser) {
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        user.setEmail(updatedUser.getEmail());
        user.setUsername(updatedUser.getUsername());

        userRepository.save(user);
    }
}
