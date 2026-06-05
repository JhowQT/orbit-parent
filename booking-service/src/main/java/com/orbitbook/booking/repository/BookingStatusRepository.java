package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingStatusRepository
        extends JpaRepository<BookingStatus, Long> {

    Optional<BookingStatus> findByName(String name);

    boolean existsByName(String name);
}