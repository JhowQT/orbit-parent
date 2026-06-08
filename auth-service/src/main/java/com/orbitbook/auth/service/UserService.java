package com.orbitbook.auth.service;

import com.orbitbook.auth.dto.UserResponseDTO;
import com.orbitbook.auth.entity.UserOrbit;
import com.orbitbook.auth.exception.ResourceNotFoundException;
import com.orbitbook.auth.mapper.UserMapper;
import com.orbitbook.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponseDTO findById(Long id) {

        UserOrbit user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuário não encontrado"
                                ));

        return userMapper.toUserResponseDTO(user);
    }

    public UserResponseDTO findByEmail(String email) {

        UserOrbit user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuário não encontrado"
                                ));

        return userMapper.toUserResponseDTO(user);
    }

    public List<UserResponseDTO> findAll() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }

    public void delete(Long id) {

        UserOrbit user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuário não encontrado"
                                ));

        userRepository.delete(user);
    }
}