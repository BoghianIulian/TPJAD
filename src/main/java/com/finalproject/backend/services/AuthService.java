package com.finalproject.backend.services;

import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.UserRepository;
import com.finalproject.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public String login(String username, String password) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Wrong username or password");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User does not exist");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        return tokenProvider.generateToken(
                user.getUsername(),
                user.getRole().name(),
                user.getId()
        );
    }
}

