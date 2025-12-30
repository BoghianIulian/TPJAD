package com.finalproject.backend.services;

import com.finalproject.backend.dto.RegisterRequest;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.exceptions.EntityValidationException;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User register(RegisterRequest dto) {

        // verificare dacă username există
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new EntityValidationException("Username already exists: " + dto.getUsername());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        return userRepository.save(user);
    }
}
