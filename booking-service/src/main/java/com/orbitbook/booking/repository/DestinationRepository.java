package com.orbitbook.booking.repository;

import com.orbitbook.booking.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    List<Destination> findByNameContainingIgnoreCase(String name);

    List<Destination> findByCapacityGreaterThanEqual(Integer capacity);

    @Query("SELECT d FROM Destination d WHERE " +
           "(:tipo IS NULL OR LOWER(d.destinationType.name) = LOWER(:tipo)) AND " +
           "(:precoMin IS NULL OR d.basePrice >= :precoMin) AND " +
           "(:precoMax IS NULL OR d.basePrice <= :precoMax) AND " +
           "(:busca IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :busca, '%')) " +
           "   OR LOWER(d.description) LIKE LOWER(CONCAT('%', :busca, '%')))")
    Page<Destination> findFiltered(
            @Param("tipo") String tipo,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax,
            @Param("busca") String busca,
            Pageable pageable
    );
}