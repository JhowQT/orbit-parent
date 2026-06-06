package com.orbitbook.booking.feign.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long idUsersOrbit;

    private String name;

    private String email;

    private LocalDateTime createdAt;

}