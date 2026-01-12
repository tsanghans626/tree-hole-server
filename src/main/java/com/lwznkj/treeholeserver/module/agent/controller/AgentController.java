package com.lwznkj.treeholeserver.module.agent.controller;

import com.lwznkj.treeholeserver.common.result.R;
import com.lwznkj.treeholeserver.module.agent.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agent.dto.AgentChatResp;
import com.lwznkj.treeholeserver.module.agent.service.IAgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
