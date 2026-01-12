package com.lwznkj.treeholeserver.module.auth.controller;

import com.lwznkj.treeholeserver.common.dto.R;
import com.lwznkj.treeholeserver.module.auth.dto.TokenResp;
import com.lwznkj.treeholeserver.module.auth.service.IAuthService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/debug")
@Profile({"dev", "debug"})
public class AuthDebugController {

    private final IAuthService authService;

    public AuthDebugController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Debug login endpoint
     * <p>
     * Usage:
     * POST /api/v1/auth/debug/login?username=testuser
     *
     * @param username username
     * @return Bearer Token
     */
    @PostMapping("/login")
    public R<TokenResp> login(@RequestParam String username) {
        TokenResp response = authService.debugLogin(username);
        return R.ok(response);
    }
}
