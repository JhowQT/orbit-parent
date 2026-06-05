package com.orbitbook.auth.service;

import com.orbitbook.auth.dto.AuthResponseDTO;
import com.orbitbook.auth.dto.LoginDTO;
import com.orbitbook.auth.dto.RegisterDTO;
import com.orbitbook.auth.dto.UserResponseDTO;
import com.orbitbook.auth.entity.Role;
import com.orbitbook.auth.entity.UserOrbit;
import com.orbitbook.auth.exception.InvalidCredentialsException;
import com.orbitbook.auth.exception.ResourceNotFoundException;
import com.orbitbook.auth.exception.UserAlreadyExistsException;
import com.orbitbook.auth.mapper.UserMapper;
import com.orbitbook.auth.repository.RoleRepository;
import com.orbitbook.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserResponseDTO register(RegisterDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Email já cadastrado"
            );
        }

        Role role = roleRepository
                .findByNameRole(dto.getRoleName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role não encontrada"
                        )
                );

        UserOrbit user = UserOrbit.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash(
                        passwordEncoder.encode(
                                dto.getPassword()
                        )
                )
                .createdAt(LocalDateTime.now())
                .role(role)
                .build();

        user = userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    public AuthResponseDTO login(LoginDTO dto) {

        UserOrbit user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Email ou senha inválidos"
                        )
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPasswordHash()
                );

        if (!passwordMatches) {
            throw new InvalidCredentialsException(
                    "Email ou senha inválidos"
            );
        }

        UserDetails userDetails =
                new User(
                        user.getEmail(),
                        user.getPasswordHash(),
                        List.of()
                );

        String token =
                jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .user(
                        userMapper.toUserResponseDTO(user)
                )
                .build();
    }
}