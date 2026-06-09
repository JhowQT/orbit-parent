package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByBooking_IdBookings(
            Long bookingId
    );

    List<Review> findByRating(Integer rating);

    @Query("SELECT r FROM Review r JOIN r.booking b WHERE b.destination.idDestinations = :destinationId")
    List<Review> findByDestinationId(
            @Param("destinationId") Long destinationId
    );
}