package com.orbitbook.authservice.repository;

import com.orbitbook.authservice.entity.UserOrbit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserOrbit, Integer> {

    Optional<UserOrbit> findByEmail(String email);

    boolean existsByEmail(String email);

}