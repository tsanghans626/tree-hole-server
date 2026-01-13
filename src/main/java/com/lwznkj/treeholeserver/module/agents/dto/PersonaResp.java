package com.lwznkj.treeholeserver.module.agents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 人设响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResp {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 系统提示词
     */
    private String systemPrompt;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 最大token数
     */
    private Integer maxTokens;

    /**
     * 分类
     */
    private String category;

    /**
     * 版本
     */
    private String version;

    /**
     * 是否激活
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
