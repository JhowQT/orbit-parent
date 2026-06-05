package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.DestinationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationTypeRepository
        extends JpaRepository<DestinationType, Long> {

    Optional<DestinationType> findByName(String name);

    boolean existsByName(String name);
}