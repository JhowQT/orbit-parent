package com.orbitbook.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DESTINATIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_destinations")
    @SequenceGenerator(name = "seq_destinations", sequenceName = "SEQ_DESTINATIONS", allocationSize = 1)
    @Column(name = "ID_DESTINATIONS")
    private Long idDestinations;

    @Column(name = "NAME", nullable = false, length = 260)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;

    @Column(name = "DISTANCE_KM", nullable = false)
    private Integer distanceKm;

    @Column(name = "BASE_PRICE", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;

    @Column(name = "IMAGE_URL", nullable = false, length = 200)
    private String imageUrl;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ID_DESTINATION_TYPES",
            nullable = false
    )
    private DestinationType destinationType;

    @OneToMany(mappedBy = "destination")
    private List<Booking> bookings;
}