package com.lwznkj.treeholeserver.module.agents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 人设列表响应DTO（分页）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaListResp {

    /**
     * 人设列表
     */
    private List<PersonaResp> content;

    /**
     * 总记录数
     */
    private Long totalElements;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
