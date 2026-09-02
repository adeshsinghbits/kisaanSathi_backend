package com.kissansathi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtConfig {
    /** Base64 or plain secret used to sign tokens. Must be at least 256 bits for HS256. */
    private String secret;
    private long expirationMs;
    private long refreshExpirationMs;
    private String issuer;
}