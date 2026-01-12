package com.lwznkj.treeholeserver.module.agent.service;

import com.lwznkj.treeholeserver.module.agent.dto.AgentChatReq;
import com.lwznkj.treeholeserver.module.agent.dto.AgentChatResp;

public interface IAgentService {
    /**
     * AI 对话接口
     *
     * @param req 对话请求
     * @return AI 回复
     */
    AgentChatResp chat(AgentChatReq req);
}
