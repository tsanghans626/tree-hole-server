package com.lwznkj.treeholeserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Authentication white list configuration
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth.white-list")
public class AuthWhiteListProperties {

    /**
     * Paths that do not require authentication
     */
    private List<String> paths = new ArrayList<>();

    /**
     * Get all white list paths
     */
    public List<String> getPaths() {
        return paths != null ? paths : new ArrayList<>();
    }
}
