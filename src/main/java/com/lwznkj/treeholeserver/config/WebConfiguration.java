package com.lwznkj.treeholeserver.config;

import com.lwznkj.treeholeserver.module.auth.interceptor.BearerTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final BearerTokenInterceptor bearerTokenInterceptor;

    public WebConfiguration(BearerTokenInterceptor bearerTokenInterceptor) {
        this.bearerTokenInterceptor = bearerTokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerTokenInterceptor)
                .addPathPatterns("/api/**")
                .order(1);
    }
}
