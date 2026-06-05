package com.orbitbook.auth.repository;

import com.orbitbook.auth.entity.UserOrbit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserOrbit, Long> {

    Optional<UserOrbit> findByEmail(String email);

    boolean existsByEmail(String email);

}