package com.orbitbook.booking.feign;

import com.orbitbook.booking.config.FeignConfig;
import com.orbitbook.booking.feign.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "auth-service",
        configuration = FeignConfig.class
)
public interface AuthClient {

    @GetMapping("/users/{id}")
    UserResponseDTO findUserById(
            @PathVariable("id") Long id
    );
}