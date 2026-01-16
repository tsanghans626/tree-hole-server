package com.lwznkj.treeholeserver.config;

import com.lwznkj.treeholeserver.ai.embedding.HunyuanEmbeddingModel;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuration for Spring AI VectorStore with PgVector.
 *
 * <p>This configuration provides vector-based semantic search for chat memory,
 * enabling the AI to retrieve relevant historical messages based on semantic similarity.
 *
 * <p>The vector store works alongside MessageWindowChatMemory:
 * <ul>
 *   <li><b>MessageWindowChatMemory</b>: Keeps the last N messages (short-term memory)</li>
 *   <li><b>VectorStore</b>: Provides semantic search across conversation history (long-term memory)</li>
 * </ul>
 */
@EnableAutoConfiguration(exclude = PgVectorStoreAutoConfiguration.class)
@Configuration
public class VectorStoreConfiguration {

    /**
     * Configure HunyuanEmbeddingModel for Tencent Hunyuan Embedding API.
     *
     * @param secretId Tencent Cloud Secret ID
     * @param secretKey Tencent Cloud Secret Key
     * @param region Tencent Cloud region
     * @return Configured EmbeddingModel
     */
    @Bean
    public EmbeddingModel hunyuanEmbeddingModel(
            @Value("${tencent.cloud.secret-id}") String secretId,
            @Value("${tencent.cloud.secret-key}") String secretKey,
            @Value("${tencent.cloud.region:ap-guangzhou}") String region) {
        return new HunyuanEmbeddingModel(secretId, secretKey, region);
    }

    /**
     * Configure PgVectorStore for semantic search.
     *
     * @param dataSource DataSource for database access
     * @param embeddingModel Embedding model for vector generation
     * @return Configured VectorStore
     */
    @Bean
    public VectorStore vectorStore(
            DataSource dataSource,
            @Qualifier("hunyuanEmbeddingModel") EmbeddingModel embeddingModel) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .initializeSchema(true)
                .build();
    }

    /**
     * Configure VectorStoreChatMemoryAdvisor.
     *
     * <p>This advisor retrieves conversation history from vector store based on semantic similarity.
     * It complements the MessageChatMemoryAdvisor which provides recent messages.
     *
     * @param vectorStore Vector store for semantic search
     * @return Configured VectorStoreChatMemoryAdvisor
     */
    @Bean
    public VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor(VectorStore vectorStore) {
        return VectorStoreChatMemoryAdvisor.builder(vectorStore)
                .defaultTopK(5)  // Retrieve top 5 most relevant historical messages
                .build();
    }
}
