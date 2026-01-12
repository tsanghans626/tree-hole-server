package com.lwznkj.treeholeserver.module.agents.service;

import com.lwznkj.treeholeserver.module.agents.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agents.dto.AgentChatResp;
import reactor.core.publisher.Flux;

public interface IAgentService {
    /**
     * AI 对话接口
     *
     * @param req 对话请求
     * @return AI 回复
     */
    AgentChatResp chat(AgentChatReq req);

    /**
     * AI 流式对话接口
     *
     * @param req 对话请求
     * @return AI 回复流
     */
    Flux<String> chatStream(AgentChatReq req);
}
