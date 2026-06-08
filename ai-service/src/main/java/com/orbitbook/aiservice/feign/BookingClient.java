package com.orbitbook.aiservice.feign;

import com.orbitbook.aiservice.dto.DestinationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/destinations/all")
    List<DestinationDTO> getAllDestinations();

    @GetMapping("/bookings/user/{userId}")
    List<BookingResponseDTO> getBookingsByUser(
            @PathVariable("userId") Long userId);

    @GetMapping("/destinations/{id}")
    DestinationDTO getDestinationById(
            @PathVariable("id") Long id);

}