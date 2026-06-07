package com.orbitbook.aiservice.feign;

import com.orbitbook.aiservice.feign.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/users/{id}")
    UserResponseDTO findUserById(
            @PathVariable("id") Long id
    );
}