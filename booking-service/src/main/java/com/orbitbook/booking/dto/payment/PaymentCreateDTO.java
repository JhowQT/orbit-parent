package com.orbitbook.booking.dto.payment;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateDTO {

    private String method;

    private BigDecimal amount;

    private String status;

    private Long bookingId;
}