package com.orbitbook.auth.controller;

import com.orbitbook.auth.dto.UserResponseDTO;
import com.orbitbook.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {

        return ResponseEntity.ok(
                userService.findAll()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(
            Authentication authentication) {

        return ResponseEntity.ok(
                userService.findByEmail(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.findById(id)
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                userService.findByEmail(email)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}