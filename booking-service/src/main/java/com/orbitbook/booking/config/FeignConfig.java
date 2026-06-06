package com.orbitbook.booking.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(
        basePackages = "com.orbitbook.booking.feign"
)
public class FeignConfig {
}