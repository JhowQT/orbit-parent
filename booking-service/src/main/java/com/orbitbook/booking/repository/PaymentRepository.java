package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByBooking_IdBookings(
            Long bookingId
    );

    List<Payment> findByStatus(String status);
}