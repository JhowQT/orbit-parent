package com.orbitbook.aiservice.feign;

import com.orbitbook.aiservice.dto.DestinationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/destinations")
    List<DestinationDTO> getAllDestinations();

}