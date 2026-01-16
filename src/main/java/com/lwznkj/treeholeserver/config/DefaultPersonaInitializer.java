package com.lwznkj.treeholeserver.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import com.lwznkj.treeholeserver.module.agents.mapper.PersonaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultPersonaInitializer {

    private final PersonaMapper personaMapper;
    private final DefaultPersonaProperties defaultPersonaProperties;

    public DefaultPersonaInitializer(PersonaMapper personaMapper, DefaultPersonaProperties defaultPersonaProperties) {
        this.personaMapper = personaMapper;
        this.defaultPersonaProperties = defaultPersonaProperties;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {initializeDefaultPersona();}

    private void initializeDefaultPersona() {
        LambdaQueryWrapper<Persona> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Persona::getCode, defaultPersonaProperties.getCode());

        Persona existingPersona = personaMapper.selectOne(wrapper);

        if (existingPersona == null) {
            Persona persona = new Persona();
            BeanUtils.copyProperties(defaultPersonaProperties, persona);
            personaMapper.insert(persona);
            log.info("默认人设初始化成功 - 唯一标识: {}, 名称: {}", defaultPersonaProperties.getCode(), persona.getName());
        } else {
            log.info("默认人设已存在 - 唯一标识: {}", existingPersona.getName());
        }
    }
}
