package com.lwznkj.treeholeserver.module.auth.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User information extracted from JWT token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    /**
     * User ID
     */
    private Long userId;

    /**
     * Username
     */
    private String username;
}
