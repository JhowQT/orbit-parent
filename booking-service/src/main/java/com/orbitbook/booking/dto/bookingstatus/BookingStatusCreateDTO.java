package com.orbitbook.booking.dto.bookingstatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusCreateDTO {

    private String name;

    private String description;
}