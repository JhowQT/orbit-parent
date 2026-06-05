package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository
        extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}