package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.payment.PaymentCreateDTO;
import com.orbitbook.booking.dto.payment.PaymentResponseDTO;
import com.orbitbook.booking.dto.payment.PaymentUpdateDTO;
import com.orbitbook.booking.entity.Booking;
import com.orbitbook.booking.entity.BookingStatus;
import com.orbitbook.booking.entity.Payment;
import com.orbitbook.booking.exception.InvalidPaymentException;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.mapper.PaymentMapper;
import com.orbitbook.booking.messaging.PaymentProducer;
import com.orbitbook.booking.messaging.dto.PaymentApprovedEvent;
import com.orbitbook.booking.repository.BookingRepository;
import com.orbitbook.booking.repository.BookingStatusRepository;
import com.orbitbook.booking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository repository;

    private final BookingRepository bookingRepository;

    private final BookingStatusRepository bookingStatusRepository;

    private final PaymentMapper mapper;

    private final PaymentProducer paymentProducer;

    public PaymentResponseDTO createPayment(
            PaymentCreateDTO dto) {

        Booking booking =
                bookingRepository.findById(
                                dto.getBookingId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + dto.getBookingId()
                                )
                        );

        if (dto.getAmount() == null
                || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPaymentException(
                    "Valor do pagamento inválido."
            );
        }

        if (dto.getAmount().compareTo(
                booking.getTotalPrice()) != 0) {

            throw new InvalidPaymentException(
                    "O valor informado deve ser igual ao valor da reserva."
            );
        }

        Payment payment =
                mapper.toEntity(dto);

        payment.setBooking(
                booking
        );

        payment.setPaidAt(
                LocalDateTime.now()
        );

        Payment saved =
                repository.save(payment);

        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO findById(
            Long id) {

        Payment payment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pagamento não encontrado. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                payment
        );
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> findByBooking(
            Long bookingId) {

        return repository
                .findByBooking_IdBookings(
                        bookingId
                )
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public PaymentResponseDTO update(
            Long id,
            PaymentUpdateDTO dto) {

        Payment payment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pagamento não encontrado. ID: "
                                                + id
                                )
                        );

        if (dto.getAmount() == null
                || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPaymentException(
                    "Valor do pagamento inválido."
            );
        }

        payment.setMethod(
                dto.getMethod()
        );

        payment.setAmount(
                dto.getAmount()
        );

        payment.setStatus(
                dto.getStatus()
        );

        Payment updated =
                repository.save(payment);

        return mapper.toResponseDTO(updated);
    }

    public PaymentResponseDTO approvePayment(
            Long id) {

        Payment payment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pagamento não encontrado. ID: "
                                                + id
                                )
                        );

        if ("APPROVED".equalsIgnoreCase(
                payment.getStatus())) {

            throw new InvalidPaymentException(
                    "Pagamento já aprovado."
            );
        }

        payment.setStatus(
                "APPROVED"
        );

        payment.setPaidAt(
                LocalDateTime.now()
        );

        Payment updated =
                repository.save(payment);

        Booking booking =
                updated.getBooking();

        BookingStatus confirmedStatus =
                bookingStatusRepository
                        .findByName(
                                "CONFIRMED"
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status CONFIRMED não encontrado."
                                )
                        );

        booking.setBookingStatus(
                confirmedStatus
        );

        bookingRepository.save(
                booking
        );

        paymentProducer.sendPaymentApproved(
                PaymentApprovedEvent.builder()
                        .paymentId(
                                updated.getIdPayments()
                        )
                        .bookingId(
                                updated.getBooking()
                                        .getIdBookings()
                        )
                        .amount(
                                updated.getAmount()
                        )
                        .approvedAt(
                                updated.getPaidAt()
                        )
                        .build()
        );

        return mapper.toResponseDTO(
                updated
        );
    }

    public void delete(
            Long id) {

        Payment payment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pagamento não encontrado. ID: "
                                                + id
                                )
                        );

        repository.delete(
                payment
        );
    }
}