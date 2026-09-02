package com.kissansathi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.cors")
@Getter
@Setter
public class CorsProperties {
    /** Comma-separated list of allowed origins, bound from app.cors.allowed-origins. */
    private List<String> allowedOrigins = new ArrayList<>();
}