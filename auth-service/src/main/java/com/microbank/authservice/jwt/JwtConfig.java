package com.microbank.authservice.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "microbank.jwt")
public class JwtConfig {

    private String secretKey;
    private long expiration;
}