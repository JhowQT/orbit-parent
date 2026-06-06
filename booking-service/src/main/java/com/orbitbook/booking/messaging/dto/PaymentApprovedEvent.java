package com.orbitbook.booking.messaging.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentApprovedEvent {

    private Long paymentId;

    private Long bookingId;

    private BigDecimal amount;

    private LocalDateTime approvedAt;
}