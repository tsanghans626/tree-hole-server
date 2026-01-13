package com.lwznkj.treeholeserver.module.agents.service.impl;

import com.lwznkj.treeholeserver.module.agents.dto.CreatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaListResp;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaQueryReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaResp;
import com.lwznkj.treeholeserver.module.agents.dto.UpdatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import com.lwznkj.treeholeserver.module.agents.repository.PersonaRepository;
import com.lwznkj.treeholeserver.module.agents.service.IPersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonaService implements IPersonaService {

    private final PersonaRepository personaRepository;

    private static final Persona DEFAULT_PERSONA = createDefaultPersona();

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    @Cacheable(value = "persona", key = "#code", unless = "#result == null")
    public Persona getByCode(String code) {
        log.debug("Fetching persona by code: {}", code);
        return personaRepository.findByCodeAndIsActiveTrue(code)
                .orElse(getDefaultPersona());
    }

    @Override
    @Cacheable(value = "personaDefault", unless = "#result == null")
    public Persona getDefaultPersona() {
        log.debug("Fetching default persona");
        return personaRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElse(DEFAULT_PERSONA);
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
        if (personaRepository.findByCode(req.getCode()).isPresent()) {
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
        return personaRepository.save(persona);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"persona", "personaDefault"}, allEntries = true)
    public Persona update(Long id, UpdatePersonaReq req) {
        log.info("Updating persona with id: {}", id);
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona not found with id: " + id));
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
        return personaRepository.save(persona);
    }

    @Override
    public Persona getById(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona not found with id: " + id));
    }

    @Override
    public PersonaListResp list(PersonaQueryReq req) {
        int page = req.getPage() != null ? req.getPage() : 0;
        int size = req.getSize() != null ? req.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Persona> personaPage;
        if (req.getIsActive() != null && req.getCategory() != null) {
            personaPage = personaRepository.findByIsActiveAndCategory(req.getIsActive(), req.getCategory(), pageable);
        } else if (req.getIsActive() != null) {
            personaPage = personaRepository.findByIsActive(req.getIsActive(), pageable);
        } else if (req.getCategory() != null) {
            personaPage = personaRepository.findByCategory(req.getCategory(), pageable);
        } else {
            personaPage = personaRepository.findAll(pageable);
        }

        List<PersonaResp> content = personaPage.getContent().stream()
                .map(this::toResp)
                .collect(Collectors.toList());

        return new PersonaListResp(
                content,
                personaPage.getTotalElements(),
                personaPage.getTotalPages(),
                page,
                size
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = {"persona", "personaDefault"}, allEntries = true)
    public void delete(Long id) {
        log.info("Soft deleting persona with id: {}", id);
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona not found with id: " + id));
        persona.setIsActive(false);
        personaRepository.save(persona);
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
