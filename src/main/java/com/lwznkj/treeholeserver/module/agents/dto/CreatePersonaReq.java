package com.lwznkj.treeholeserver.module.agents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建人设请求DTO
 */
@Data
public class CreatePersonaReq {

    /**
     * 唯一标识
     */
    @NotBlank(message = "code不能为空")
    @Size(max = 50, message = "code长度不能超过50")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "name不能为空")
    @Size(max = 100, message = "name长度不能超过100")
    private String name;

    /**
     * 描述
     */
    @Size(max = 500, message = "description长度不能超过500")
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
    @Size(max = 50, message = "category长度不能超过50")
    private String category;

    /**
     * 版本
     */
    private String version;
}
