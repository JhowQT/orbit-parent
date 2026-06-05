package com.orbitbook.booking.dto.passenger;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerCreateDTO {

    private LocalDate dataNasc;

    private String cpf;

    private String name;

    private Long bookingId;
}