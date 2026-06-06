package com.orbitbook.booking.feign;

import com.orbitbook.booking.feign.dto.AiRecommendationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ai-service"
)
public interface AiClient {

    @GetMapping("/recommendations/{id}")
    AiRecommendationDTO findRecommendationById(
            @PathVariable("id") Long id
    );
}