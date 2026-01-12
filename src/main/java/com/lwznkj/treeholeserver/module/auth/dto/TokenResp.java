package com.lwznkj.treeholeserver.module.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResp {
    /**
     * JWT Bearer Token
     */
    private String token;

    /**
     * Token type, fixed as Bearer
     */
    private String tokenType = "Bearer";

    /**
     * User id
     */
    private Long userId;

    /**
     * Username
     */
    private String username;
}
