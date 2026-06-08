package com.orbitbook.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "REVIEWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reviews")
    @SequenceGenerator(name = "seq_reviews", sequenceName = "SEQ_REVIEWS", allocationSize = 1)
    @Column(name = "ID_REVIEWS")
    private Long idReviews;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "COMENTARIO", length = 300)
    private String comment;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_BOOKINGS")
    private Booking booking;
}