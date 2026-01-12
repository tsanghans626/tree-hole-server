package com.lwznkj.treeholeserver.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson configuration to handle edge cases in API responses.
 *
 * <p>This configuration fixes an issue where the Hunyuan (腾讯混元) API and some other
 * AI providers return empty strings for enum fields (e.g., "role":"") in streaming
 * chat completion chunks, which causes Jackson deserialization to fail.
 *
 * <p>Related Spring AI issue: https://github.com/spring-projects/spring-ai/issues/2222
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Configure ModelOptionsUtils' ObjectMapper to accept empty strings as null objects.
     * This allows deserialization of enum fields with empty string values.
     */
    @PostConstruct
    public void configureModelOptionsObjectMapper() {
        ModelOptionsUtils.OBJECT_MAPPER.configure(
            DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true
        );
    }
}
