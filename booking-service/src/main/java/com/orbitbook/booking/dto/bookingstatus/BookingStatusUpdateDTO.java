package com.orbitbook.booking.dto.bookingstatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusUpdateDTO {

    private String name;

    private String description;
}