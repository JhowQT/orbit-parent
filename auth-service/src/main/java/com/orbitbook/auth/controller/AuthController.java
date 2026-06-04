package com.orbitbook.auth.controller;

import com.orbitbook.auth.dto.AuthResponseDTO;
import com.orbitbook.auth.dto.LoginDTO;
import com.orbitbook.auth.dto.RegisterDTO;
import com.orbitbook.auth.dto.UserResponseDTO;
import com.orbitbook.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody RegisterDTO dto) {

        UserResponseDTO response =
                authService.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginDTO dto) {

        AuthResponseDTO response =
                authService.login(dto);

        return ResponseEntity.ok(response);
    }
}