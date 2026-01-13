package com.lwznkj.treeholeserver.module.agents.service;

import com.lwznkj.treeholeserver.module.agents.dto.CreatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaListResp;
import com.lwznkj.treeholeserver.module.agents.dto.PersonaQueryReq;
import com.lwznkj.treeholeserver.module.agents.dto.UpdatePersonaReq;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import org.springframework.data.domain.Page;

public interface IPersonaService {

    /**
     * Get persona by code (with caching).
     *
     * @param code persona unique identifier
     * @return persona info, returns default persona if not found or inactive
     */
    Persona getByCode(String code);

    /**
     * Get default persona (with caching).
     *
     * @return the first active persona
     */
    Persona getDefaultPersona();

    /**
     * Evict cache for specific persona.
     *
     * @param code persona code
     */
    void evictCache(String code);

    /**
     * Evict all persona cache.
     */
    void evictAllCache();

    /**
     * Create a new persona.
     *
     * @param req create request
     * @return created persona
     */
    Persona create(CreatePersonaReq req);

    /**
     * Update an existing persona.
     *
     * @param id  persona id
     * @param req update request
     * @return updated persona
     */
    Persona update(Long id, UpdatePersonaReq req);

    /**
     * Get persona by id.
     *
     * @param id persona id
     * @return persona info
     */
    Persona getById(Long id);

    /**
     * List personas with pagination and filters.
     *
     * @param req query request
     * @return paged persona list
     */
    PersonaListResp list(PersonaQueryReq req);

    /**
     * Soft delete a persona (set isActive to false).
     *
     * @param id persona id
     */
    void delete(Long id);
}
