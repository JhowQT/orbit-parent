package com.orbitbook.booking.dto.payment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long idPayments;

    private String method;

    private BigDecimal amount;

    private String status;

    private LocalDateTime paidAt;

    private Long bookingId;
}