package com.lwznkj.treeholeserver.module.agents.dto;

import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 人设响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaAdminResp {

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
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后更新人
     */
    private Long updateBy;

    public static PersonaAdminResp fromEntity(Persona persona) {
        if (persona == null) return null;
        PersonaAdminResp resp = new PersonaAdminResp();
        BeanUtils.copyProperties(persona, resp);
        return resp;
    }
}
