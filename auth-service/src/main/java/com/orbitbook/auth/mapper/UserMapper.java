package com.orbitbook.auth.mapper;

import com.orbitbook.auth.dto.RoleResponseDTO;
import com.orbitbook.auth.dto.UserResponseDTO;
import com.orbitbook.auth.entity.Role;
import com.orbitbook.auth.entity.UserOrbit;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toUserResponseDTO(UserOrbit user) {

        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .idUsersOrbit(user.getIdUsersOrbit())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .role(toRoleResponseDTO(user.getRole()))
                .build();
    }

    public RoleResponseDTO toRoleResponseDTO(Role role) {

        if (role == null) {
            return null;
        }

        return RoleResponseDTO.builder()
                .idRole(role.getIdRole())
                .nameRole(role.getNameRole())
                .description(role.getDescription())
                .build();
    }
}