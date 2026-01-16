package com.lwznkj.treeholeserver.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lwznkj.treeholeserver.common.util.PasswordUtil;
import com.lwznkj.treeholeserver.module.users.entity.User;
import com.lwznkj.treeholeserver.module.users.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 超级管理员初始化器
 * 应用启动时自动检查并创建超级管理员
 */
@Slf4j
@Component
public class SuperAdminInitializer {

    private final UserMapper userMapper;
    private final SuperAdminProperties superAdminProperties;

    public SuperAdminInitializer(UserMapper userMapper, SuperAdminProperties superAdminProperties) {
        this.userMapper = userMapper;
        this.superAdminProperties = superAdminProperties;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        initializeSuperAdmin();
    }

    /**
     * 初始化超级管理员
     */
    private void initializeSuperAdmin() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, superAdminProperties.getUsername());

        User existingAdmin = userMapper.selectOne(wrapper);

        if (existingAdmin == null) {
            // 创建超级管理员
            User superAdmin = new User();
            superAdmin.setUsername(superAdminProperties.getUsername());
            superAdmin.setName(superAdminProperties.getName());
            superAdmin.setPassword(PasswordUtil.encode(superAdminProperties.getPassword()));
            superAdmin.setIsSuperAdmin(true);

            userMapper.insert(superAdmin);
            log.info("超级管理员初始化成功 - 用户名: {}, 昵称: {}",
                    superAdmin.getUsername(), superAdmin.getName());
        } else {
            log.info("超级管理员已存在 - 用户名: {}", existingAdmin.getUsername());
        }
    }
}
