package com.lwznkj.treeholeserver.module.agents.dto;

import lombok.Data;

@Data
public class AgentChatReq {
    /**
     * 用户消息内容
     */
    private String message;

    /**
     * 人设 code，可选。
     * 如果不指定，则使用默认人设
     */
    private String personaCode;
}
