package com.lwznkj.treeholeserver.module.agents.entity;

import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.annotation.pgsql.PgsqlTypeConstant;
import com.tangzc.mpe.autotable.annotation.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.mpe.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table("personas")
public class Persona extends BaseEntity<Long, LocalDateTime> {

    @ColumnId(value = "id", mode = IdType.AUTO)
    private Long id;

    @ColumnComment("标识")
    @Column(value = "code")
    private String code;

    @ColumnComment("名称")
    @Column(value = "name")
    private String name;

    @ColumnComment("描述")
    @Column(value = "description", length = 500)
    private String description;

    @ColumnComment("系统提示词")
    @ColumnType(PgsqlTypeConstant.TEXT)
    @Column(value = "system_prompt")
    private String systemPrompt;

    @ColumnComment("温度")
    @Column(value = "temperature")
    private Double temperature;

    @ColumnComment("最大token限制")
    @Column(value = "max_tokens")
    private Integer maxTokens;

    @ColumnComment("分类")
    @Column(value = "category")
    private String category;

    @ColumnComment("版本")
    @Column(value = "version")
    private String version;

    @ColumnComment("是否启用")
    @Column(value = "is_active")
    private Boolean isActive = true;
}
