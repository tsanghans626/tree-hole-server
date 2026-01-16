package com.lwznkj.treeholeserver.module.agents.service.impl;

import com.lwznkj.treeholeserver.module.agents.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agents.dto.AgentChatResp;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import com.lwznkj.treeholeserver.module.agents.service.IAgentService;
import com.lwznkj.treeholeserver.module.agents.service.IPersonaService;
import com.lwznkj.treeholeserver.module.auth.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AgentService implements IAgentService {

    private final ChatClient.Builder chatClientBuilder;
    private final IPersonaService personaService;
    private final ChatMemory chatMemory;
    private final VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor;

    public AgentService(
            ChatClient.Builder chatClientBuilder,
            IPersonaService personaService,
            ChatMemory chatMemory,
            VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor) {
        this.chatClientBuilder = chatClientBuilder;
        this.personaService = personaService;
        this.chatMemory = chatMemory;
        this.vectorStoreChatMemoryAdvisor = vectorStoreChatMemoryAdvisor;
    }

    /**
     * Build a ChatClient with dual memory advisors for a specific conversation.
     *
     * <p>The ChatClient is configured with two advisors:
     * <ul>
     *   <li><b>MessageChatMemoryAdvisor</b>: Provides recent conversation context (short-term memory)</li>
     *   <li><b>VectorStoreChatMemoryAdvisor</b>: Provides semantically relevant historical context (long-term memory)</li>
     * </ul>
     *
     * @param conversationId Unique identifier for the conversation
     * @return Configured ChatClient with both memory advisors
     */
    private ChatClient buildChatClientWithMemory(String conversationId) {
        return chatClientBuilder
                .defaultAdvisors(
                        // Short-term memory: Recent messages from JDBC store
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build(),
                        // Long-term memory: Semantic search across conversation history
                        // Note: VectorStoreChatMemoryAdvisor will use the conversationId from ChatClient's context
                        vectorStoreChatMemoryAdvisor
                )
                .build();
    }

    @Override
    public AgentChatResp chat(AgentChatReq req) {
        String conversationId = generateConversationId(req);
        ChatClient chatClient = buildChatClientWithMemory(conversationId);
        Persona persona = resolvePersona(req.getPersonaCode());

        var promptBuilder = chatClient.prompt();

        // Set system prompt
        if (persona.getSystemPrompt() != null && !persona.getSystemPrompt().isBlank()) {
            promptBuilder.system(persona.getSystemPrompt());
        }

        // Set user message
        promptBuilder.user(req.getMessage());

        // Set ChatOptions (temperature, maxTokens)
        ChatOptions options = buildChatOptions(persona);
        if (options != null) {
            promptBuilder.options(options);
        }

        String reply = promptBuilder.call().content();
        return new AgentChatResp(reply);
    }

    @Override
    public Flux<String> chatStream(AgentChatReq req) {
        String conversationId = generateConversationId(req);
        ChatClient chatClient = buildChatClientWithMemory(conversationId);
        Persona persona = resolvePersona(req.getPersonaCode());

        var promptBuilder = chatClient.prompt();

        // Set system prompt
        if (persona.getSystemPrompt() != null && !persona.getSystemPrompt().isBlank()) {
            promptBuilder.system(persona.getSystemPrompt());
        }

        // Set user message
        promptBuilder.user(req.getMessage());

        // Set ChatOptions (temperature, maxTokens)
        ChatOptions options = buildChatOptions(persona);
        if (options != null) {
            promptBuilder.options(options);
        }

        return promptBuilder.stream().content();
    }

    /**
     * Resolve persona by code. If code is null or persona not found, use default persona.
     */
    private Persona resolvePersona(String personaCode) {
        if (personaCode == null || personaCode.isBlank()) {
            return personaService.getDefaultPersona();
        }
        return personaService.getByCode(personaCode);
    }

    /**
     * Build ChatOptions from persona settings.
     */
    private ChatOptions buildChatOptions(Persona persona) {
        var optionsBuilder = OpenAiChatOptions.builder();

        if (persona.getTemperature() != null) {
            optionsBuilder.temperature(persona.getTemperature().doubleValue());
        }
        if (persona.getMaxTokens() != null) {
            optionsBuilder.maxTokens(persona.getMaxTokens());
        }

        return optionsBuilder.build();
    }

    /**
     * Generate a unique conversation ID based on current user and persona.
     * This ensures each user-persona combination has a separate conversation history.
     *
     * @param req The chat request containing personaCode
     * @return Unique conversation ID string in format: {userId}:{personaCode}
     */
    private String generateConversationId(AgentChatReq req) {
        Long userId = UserContext.getCurrentUserId();
        String personaCode = (req.getPersonaCode() != null && !req.getPersonaCode().isBlank())
                ? req.getPersonaCode()
                : "default";

        return userId + ":" + personaCode;
    }
}
