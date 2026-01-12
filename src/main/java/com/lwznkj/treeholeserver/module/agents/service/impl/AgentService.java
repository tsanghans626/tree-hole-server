package com.lwznkj.treeholeserver.module.agents.service.impl;

import com.lwznkj.treeholeserver.module.agents.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agents.dto.AgentChatResp;
import com.lwznkj.treeholeserver.module.agents.service.IAgentService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AgentService implements IAgentService {

    private final ChatClient chatClient;


    public AgentService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public AgentChatResp chat(AgentChatReq req) {
        String reply = chatClient.prompt()
                .user(req.getMessage())
                .call()
                .content();
        return new AgentChatResp(reply);
    }

    @Override
    public Flux<String> chatStream(AgentChatReq req) {
        return chatClient.prompt()
                .user(req.getMessage())
                .stream()
                .content();
    }
}
