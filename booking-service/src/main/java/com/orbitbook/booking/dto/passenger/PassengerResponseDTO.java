package com.orbitbook.booking.dto.passenger;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerResponseDTO {

    private Long idPassagers;

    private LocalDateTime createdAt;

    private LocalDate dataNasc;

    private String cpf;

    private String name;

    private Long bookingId;
}