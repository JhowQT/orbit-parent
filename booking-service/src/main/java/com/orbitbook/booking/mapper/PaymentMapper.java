package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.payment.PaymentResponseDTO;
import com.orbitbook.booking.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toResponseDTO(
            Payment entity) {

        if (entity == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .idPayments(entity.getIdPayments())
                .method(entity.getMethod())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .paidAt(entity.getPaidAt())
                .bookingId(
                        entity.getBooking()
                                .getIdBookings()
                )
                .build();
    }
}