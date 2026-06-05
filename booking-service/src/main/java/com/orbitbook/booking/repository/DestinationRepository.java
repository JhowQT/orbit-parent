package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    List<Destination> findByNameContainingIgnoreCase(String name);

    List<Destination> findByCapacityGreaterThanEqual(Integer capacity);

}