package com.orbitbook.booking.dto.destination;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationPageDTO {
    private List<DestinationResponseDTO> items;
    private long total;
    private int page;
    private int pages;
    private int limit;
}
