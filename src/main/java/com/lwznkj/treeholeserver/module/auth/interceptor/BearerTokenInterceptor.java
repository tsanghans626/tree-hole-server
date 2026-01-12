package com.lwznkj.treeholeserver.module.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwznkj.treeholeserver.common.dto.R;
import com.lwznkj.treeholeserver.config.AuthWhiteListProperties;
import com.lwznkj.treeholeserver.module.auth.context.UserInfo;
import com.lwznkj.treeholeserver.module.auth.context.UserContext;
import com.lwznkj.treeholeserver.module.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.List;

/**
 * Bearer Token authentication interceptor
 */
@Component
public class BearerTokenInterceptor implements org.springframework.web.servlet.HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final AuthWhiteListProperties whiteListProperties;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BearerTokenInterceptor(JwtUtil jwtUtil, AuthWhiteListProperties whiteListProperties) {
        this.jwtUtil = jwtUtil;
        this.whiteListProperties = whiteListProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

        // Check if the path is in the white list
        if (isWhiteListed(requestUri)) {
            return true;
        }

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, "Missing or invalid Authorization header");
            return false;
        }

        // Extract token
        String token = authHeader.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return false;
        }

        // Extract user info and set to context
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            UserContext.setCurrentUser(new UserInfo(userId, username));
        } catch (Exception e) {
            sendUnauthorizedResponse(response, "Failed to parse token");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Clean up ThreadLocal to prevent memory leak
        UserContext.clear();
    }

    /**
     * Check if the request path is in the white list
     */
    private boolean isWhiteListed(String requestUri) {
        List<String> whiteList = whiteListProperties.getPaths();
        for (String pattern : whiteList) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send 401 unauthorized response
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        R<Void> result = R.fail(401, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.getWriter().flush();
    }
}
