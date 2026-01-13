package com.lwznkj.treeholeserver.module.agents.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lwznkj.treeholeserver.module.agents.dto.CreatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaListResp;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaQueryReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaResp;
import com.lwznkj.treeholeserver.module.agents.dto.UpdatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import com.lwznkj.treeholeserver.module.agents.mapper.PersonaMapper;
import com.lwznkj.treeholeserver.module.agents.service.IPersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonaService implements IPersonaService {

    private final PersonaMapper personaMapper;

    private static final Persona DEFAULT_PERSONA = createDefaultPersona();

    public PersonaService(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    @Override
    @Cacheable(value = "persona", key = "#code", unless = "#result == null")
    public Persona getByCode(String code) {
        log.debug("Fetching persona by code: {}", code);
        LambdaQueryWrapper<Persona> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Persona::getCode, code)
               .eq(Persona::getIsActive, true)
               .last("LIMIT 1");

        Persona result = personaMapper.selectOne(wrapper);
        return result != null ? result : getDefaultPersona();
    }

    @Override
    @Cacheable(value = "personaDefault", unless = "#result == null")
    public Persona getDefaultPersona() {
        log.debug("Fetching default persona");
        LambdaQueryWrapper<Persona> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Persona::getIsActive, true)
               .orderByDesc(Persona::getCreatedAt)
               .last("LIMIT 1");

        Persona result = personaMapper.selectOne(wrapper);
        return result != null ? result : DEFAULT_PERSONA;
    }

    @Override
    @CacheEvict(value = "persona", key = "#code")
    public void evictCache(String code) {
        log.info("Evicted cache for persona: {}", code);
    }

    @Override
    @CacheEvict(value = {"persona", "personaDefault"}, allEntries = true)
    public void evictAllCache() {
        log.info("Evicted all persona cache");
    }

    @Override
    @Transactional
    @CacheEvict(value = {"persona", "personaDefault"}, allEntries = true)
    public Persona create(CreatePersonaReq req) {
        log.info("Creating persona with code: {}", req.getCode());

        // Check if code already exists
        LambdaQueryWrapper<Persona> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Persona::getCode, req.getCode());
        if (personaMapper.selectCount(wrapper) > 0) {
            throw new IllegalArgumentException("Persona with code '" + req.getCode() + "' already exists");
        }

        Persona persona = new Persona();
        persona.setCode(req.getCode());
        persona.setName(req.getName());
        persona.setDescription(req.getDescription());
        persona.setSystemPrompt(req.getSystemPrompt());
        persona.setTemperature(req.getTemperature());
        persona.setMaxTokens(req.getMaxTokens());
        persona.setCategory(req.getCategory());
        persona.setVersion(req.getVersion());
        persona.setIsActive(true);

        personaMapper.insert(persona);
        return persona;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"persona", "personaDefault"}, allEntries = true)
    public Persona update(Long id, UpdatePersonaReq req) {
        log.info("Updating persona with id: {}", id);

        Persona persona = personaMapper.selectById(id);
        if (persona == null) {
            throw new IllegalArgumentException("Persona not found with id: " + id);
        }

        if (req.getName() != null) {
            persona.setName(req.getName());
        }
        if (req.getDescription() != null) {
            persona.setDescription(req.getDescription());
        }
        if (req.getSystemPrompt() != null) {
            persona.setSystemPrompt(req.getSystemPrompt());
        }
        if (req.getTemperature() != null) {
            persona.setTemperature(req.getTemperature());
        }
        if (req.getMaxTokens() != null) {
            persona.setMaxTokens(req.getMaxTokens());
        }
        if (req.getCategory() != null) {
            persona.setCategory(req.getCategory());
        }
        if (req.getVersion() != null) {
            persona.setVersion(req.getVersion());
        }
        if (req.getIsActive() != null) {
            persona.setIsActive(req.getIsActive());
        }

        personaMapper.updateById(persona);
        return persona;
    }

    @Override
    public Persona getById(Long id) {
        Persona persona = personaMapper.selectById(id);
        if (persona == null) {
            throw new IllegalArgumentException("Persona not found with id: " + id);
        }
        return persona;
    }

    @Override
    public PersonaListResp list(PersonaQueryReq req) {
        int page = req.getPage() != null ? req.getPage() : 0;
        int size = req.getSize() != null ? req.getSize() : 10;

        // Note: MyBatis Plus page starts from 1, but JPA starts from 0
        Page<Persona> pageParam = new Page<>(page + 1, size);

        LambdaQueryWrapper<Persona> wrapper = new LambdaQueryWrapper<>();

        if (req.getIsActive() != null) {
            wrapper.eq(Persona::getIsActive, req.getIsActive());
        }
        if (req.getCategory() != null) {
            wrapper.eq(Persona::getCategory, req.getCategory());
        }

        // Always order by created_at desc for consistency
        wrapper.orderByDesc(Persona::getCreatedAt);

        IPage<Persona> personaPage = personaMapper.selectPage(pageParam, wrapper);

        List<PersonaResp> content = personaPage.getRecords().stream()
                .map(this::toResp)
                .collect(Collectors.toList());

        return new PersonaListResp(
                content,
                personaPage.getTotal(),
                (int) personaPage.getPages(),
                page,
                size
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = {"persona", "personaDefault"}, allEntries = true)
    public void delete(Long id) {
        log.info("Soft deleting persona with id: {}", id);

        Persona persona = personaMapper.selectById(id);
        if (persona == null) {
            throw new IllegalArgumentException("Persona not found with id: " + id);
        }

        persona.setIsActive(false);
        personaMapper.updateById(persona);
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

    private static Persona createDefaultPersona() {
        Persona persona = new Persona();
        persona.setId(0L);
        persona.setCode("default");
        persona.setName("Default Assistant");
        persona.setDescription("Default AI assistant persona");
        persona.setSystemPrompt("You are a friendly and professional AI assistant named \"Tree Hole\". You are good at listening and providing warm support and advice.");
        persona.setTemperature(0.7);
        persona.setMaxTokens(2000);
        persona.setIsActive(true);
        return persona;
    }
}
