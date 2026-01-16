package com.lwznkj.treeholeserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 超级管理员配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "super-admin")
public class SuperAdminProperties {

    /**
     * 超级管理员用户名
     */
    private String username = "admin";

    /**
     * 超级管理员密码（明文，启动时加密）
     */
    private String password = "admin123";

    /**
     * 超级管理员昵称
     */
    private String name = "超级管理员";
}
