package com.naqqa.Ledger.services;

import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.model.UpdateUserRequest;
import com.naqqa.Ledger.model.dto.UserDto;
import com.naqqa.Ledger.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void updateUserDetails(UserEntity user, UpdateUserRequest request) {
        user.setUsername(request.username());

        userRepository.save(user);
    }

    public UserDto getCurrentUserDetails(UserEntity user) {
        return new UserDto(user.getUsername(), user.getEmail(), user.getAuthMethod());
    }
}
