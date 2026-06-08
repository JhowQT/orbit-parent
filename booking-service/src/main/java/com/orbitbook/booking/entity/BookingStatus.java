package com.orbitbook.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BOOKING_STATUSES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking_statuses")
    @SequenceGenerator(name = "seq_booking_statuses", sequenceName = "SEQ_BOOKING_STATUSES", allocationSize = 1)
    @Column(name = "ID_BOOKING_STATUSES")
    private Long idBookingStatuses;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "bookingStatus")
    private List<Booking> bookings;
}