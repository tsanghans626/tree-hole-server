package com.lwznkj.treeholeserver.module.agents.controller;

import com.lwznkj.treeholeserver.common.dto.R;
import com.lwznkj.treeholeserver.module.agents.dto.CreatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaListResp;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaQueryReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaResp;
import com.lwznkj.treeholeserver.module.agents.dto.UpdatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import com.lwznkj.treeholeserver.module.agents.service.IPersonaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 人设管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController {

    private final IPersonaService personaService;

    public PersonaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * 创建人设
     */
    @PostMapping
    public R<PersonaResp> create(@Valid @RequestBody CreatePersonaReq req) {
        log.info("Creating persona with code: {}", req.getCode());
        Persona persona = personaService.create(req);
        return R.ok(toResp(persona));
    }

    /**
     * 更新人设
     */
    @PutMapping("/{id}")
    public R<PersonaResp> update(@PathVariable Long id, @Valid @RequestBody UpdatePersonaReq req) {
        log.info("Updating persona with id: {}", id);
        Persona persona = personaService.update(id, req);
        return R.ok(toResp(persona));
    }

    /**
     * 根据ID查询人设
     */
    @GetMapping("/{id}")
    public R<PersonaResp> getById(@PathVariable Long id) {
        Persona persona = personaService.getById(id);
        return R.ok(toResp(persona));
    }

    /**
     * 根据code查询人设
     */
    @GetMapping("/code/{code}")
    public R<PersonaResp> getByCode(@PathVariable String code) {
        Persona persona = personaService.getByCode(code);
        return R.ok(toResp(persona));
    }

    /**
     * 分页列表查询
     *
     * @param isActive 激活状态过滤 (可选)
     * @param category 分类过滤 (可选)
     * @param page     页码，从0开始 (默认0)
     * @param size     每页大小 (默认10)
     */
    @GetMapping
    public R<PersonaListResp> list(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        PersonaQueryReq req = new PersonaQueryReq();
        req.setIsActive(isActive);
        req.setCategory(category);
        req.setPage(page);
        req.setSize(size);
        return R.ok(personaService.list(req));
    }

    /**
     * 软删除人设 (设为不激活)
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        log.info("Deleting persona with id: {}", id);
        personaService.delete(id);
        return R.ok();
    }

    private PersonaResp toResp(Persona persona) {
        return new PersonaResp(
                persona.getId(),
                persona.getCode(),
                persona.getName(),
                persona.getDescription(),
                persona.getSystemPrompt(),
                persona.getTemperature(),
                persona.getMaxTokens(),
                persona.getCategory(),
                persona.getVersion(),
                persona.getIsActive(),
                persona.getCreatedAt(),
                persona.getUpdatedAt()
        );
    }
}
