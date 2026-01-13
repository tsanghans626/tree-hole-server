package com.lwznkj.treeholeserver.module.agents.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("personas")
public class Persona {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "code")
    private String code;

    @TableField(value = "name")
    private String name;

    @TableField(value = "description")
    private String description;

    @TableField(value = "system_prompt")
    private String systemPrompt;

    @TableField(value = "temperature")
    private Double temperature;

    @TableField(value = "max_tokens")
    private Integer maxTokens;

    @TableField(value = "category")
    private String category;

    @TableField(value = "version")
    private String version;

    @TableField(value = "is_active")
    private Boolean isActive = true;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
