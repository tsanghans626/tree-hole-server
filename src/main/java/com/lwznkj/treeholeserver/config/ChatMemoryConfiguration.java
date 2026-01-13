package com.lwznkj.treeholeserver.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Spring AI Chat Memory.
 * Configures JDBC-based persistent chat memory with MessageWindowChatMemory.
 */
@Configuration
public class ChatMemoryConfiguration {

    /**
     * Configure ChatMemory with JDBC repository for persistent storage.
     *
     * @param chatMemoryRepository Auto-configured JdbcChatMemoryRepository
     * @return ChatMemory bean using message window strategy
     */
    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(50)
                .build();
    }
}
