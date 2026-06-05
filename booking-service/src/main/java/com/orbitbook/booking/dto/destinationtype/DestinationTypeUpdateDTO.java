package com.orbitbook.booking.dto.destinationtype;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationTypeUpdateDTO {

    private String name;

    private String description;
}