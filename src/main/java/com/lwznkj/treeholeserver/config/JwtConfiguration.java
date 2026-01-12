package com.lwznkj.treeholeserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {

    /**
     * JWT signing key
     */
    private String secret;

    /**
     * Token expiration time in milliseconds, default 24 hours
     */
    private Long expiration = 86400000L;
}
