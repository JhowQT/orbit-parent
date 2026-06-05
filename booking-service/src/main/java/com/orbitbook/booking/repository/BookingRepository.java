package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByBookingStatus_IdBookingStatuses(
            Long statusId
    );

    List<Booking> findByDestination_IdDestinations(
            Long destinationId
    );
}