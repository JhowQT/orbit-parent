package com.orbitbook.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BOOKINGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BOOKINGS")
    private Long idBookings;

    @Column(name = "DEPARTURE_DATE", nullable = false)
    private LocalDateTime departureDate;

    @Column(name = "RETURN_DATE", nullable = false)
    private LocalDateTime returnDate;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "NUM_PASSENGERS", nullable = false)
    private Integer numPassengers;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "ID_USERS_ORBIT", nullable = false)
    private Long userId;

    @Column(name = "ID_AI_RECOMENDATION", nullable = false)
    private Long aiRecommendationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ID_DESTINATIONS",
            nullable = false
    )
    private Destination destination;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ID_BOOKING_STATUSES",
            nullable = false
    )
    private BookingStatus bookingStatus;

    @OneToMany(mappedBy = "booking")
    private List<Passenger> passengers;

    @OneToMany(mappedBy = "booking")
    private List<Payment> payments;

    @OneToMany(mappedBy = "booking")
    private List<Review> reviews;
}