package com.orbitbook.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payments")
    @SequenceGenerator(name = "seq_payments", sequenceName = "SEQ_PAYMENTS", allocationSize = 1)
    @Column(name = "ID_PAYMENTS")
    private Long idPayments;

    @Column(name = "METHOD", nullable = false, length = 100)
    private String method;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "STATUS", nullable = false, length = 100)
    private String status;

    @Column(name = "PAID_AT")
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ID_BOOKINGS",
            nullable = false
    )
    private Booking booking;
}