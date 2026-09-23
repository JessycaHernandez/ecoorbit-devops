package com.ecoorbit.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Value("${app.environment:local}")
    private String environment;

    @GetMapping("/api/status")
    public Map<String, String> status() {
        return Map.of(
                "app", "ecoorbit-api",
                "status", "UP",
                "environment", environment
        );
    }
}
