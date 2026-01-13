package com.lwznkj.treeholeserver.module.agents.dto;

import lombok.Data;

/**
 * 人设查询请求DTO
 */
@Data
public class PersonaQueryReq {

    /**
     * 是否激活，null表示查询全部
     */
    private Boolean isActive;

    /**
     * 分类，null表示查询全部分类
     */
    private String category;

    /**
     * 页码，从0开始，默认0
     */
    private Integer page = 0;

    /**
     * 每页大小，默认10
     */
    private Integer size = 10;
}
