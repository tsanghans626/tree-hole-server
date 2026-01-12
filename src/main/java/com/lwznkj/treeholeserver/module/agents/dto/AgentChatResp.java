package com.lwznkj.treeholeserver.module.agents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentChatResp {
    /**
     * AI 回复内容
     */
    private String reply;
}
