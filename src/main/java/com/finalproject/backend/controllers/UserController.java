package com.finalproject.backend.controllers;

import com.finalproject.backend.dto.RegisterRequest;
import com.finalproject.backend.dto.RegisterWithCodeRequest;
import com.finalproject.backend.dto.UserResponse;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest req) {

        User u = userService.register(req);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }

    @PostMapping("/register-code")
    public UserResponse registerWithCode(@Valid @RequestBody RegisterWithCodeRequest req) {
        User u = userService.registerWithCode(req);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }
}

