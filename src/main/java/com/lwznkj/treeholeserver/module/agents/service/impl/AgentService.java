package com.lwznkj.treeholeserver.module.agents.service.impl;

import com.lwznkj.treeholeserver.module.agents.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agents.dto.AgentChatResp;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import com.lwznkj.treeholeserver.module.agents.service.IAgentService;
import com.lwznkj.treeholeserver.module.agents.service.IPersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AgentService implements IAgentService {

    private final ChatClient chatClient;
    private final IPersonaService personaService;

    public AgentService(ChatClient.Builder chatClientBuilder, IPersonaService personaService) {
        this.chatClient = chatClientBuilder.build();
        this.personaService = personaService;
    }

    @Override
    public AgentChatResp chat(AgentChatReq req) {
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
}
