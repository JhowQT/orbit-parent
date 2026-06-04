package com.orbitbook.auth.service;

import com.orbitbook.auth.dto.AuthResponseDTO;
import com.orbitbook.auth.dto.LoginDTO;
import com.orbitbook.auth.dto.RegisterDTO;
import com.orbitbook.auth.dto.UserResponseDTO;
import com.orbitbook.auth.entity.Role;
import com.orbitbook.auth.entity.UserOrbit;
import com.orbitbook.auth.mapper.UserMapper;
import com.orbitbook.auth.repository.RoleRepository;
import com.orbitbook.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(RegisterDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Role role = roleRepository.findByNameRole(dto.getRoleName())
                .orElseThrow(() ->
                        new RuntimeException("Role não encontrada"));

        UserOrbit user = UserOrbit.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .createdAt(LocalDateTime.now())
                .role(role)
                .build();

        user = userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    public AuthResponseDTO login(LoginDTO dto) {

        UserOrbit user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Email ou senha inválidos"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPasswordHash());

        if (!passwordMatches) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        return AuthResponseDTO.builder()
                .token(null)
                .user(userMapper.toUserResponseDTO(user))
                .build();
    }
}