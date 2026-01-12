package com.lwznkj.treeholeserver.module.agents.controller;

import com.lwznkj.treeholeserver.common.dto.R;
import com.lwznkj.treeholeserver.module.agents.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agents.dto.AgentChatResp;
import com.lwznkj.treeholeserver.module.agents.service.IAgentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final IAgentService agentService;

    public AgentController(IAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/chat")
    public R<AgentChatResp> chat(@RequestBody AgentChatReq req) {
        return R.ok(agentService.chat(req));
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody AgentChatReq req) {
        return agentService.chatStream(req);
    }
}
