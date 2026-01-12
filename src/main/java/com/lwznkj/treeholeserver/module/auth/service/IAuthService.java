package com.lwznkj.treeholeserver.module.auth.service;

import com.lwznkj.treeholeserver.module.auth.dto.TokenResp;

public interface IAuthService {

    /**
     * Debug login: get token by username (no password required)
     * Note: This endpoint is for debug/development only
     *
     * @param username username
     * @return Token response
     */
    TokenResp debugLogin(String username);
}
