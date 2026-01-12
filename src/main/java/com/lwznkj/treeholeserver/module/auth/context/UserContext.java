package com.lwznkj.treeholeserver.module.auth.context;

/**
 * ThreadLocal context for storing current request user information
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> CONTEXT = new ThreadLocal<>();

    /**
     * Set current user
     */
    public static void setCurrentUser(UserInfo userInfo) {
        CONTEXT.set(userInfo);
    }

    /**
     * Get current user
     */
    public static UserInfo getCurrentUser() {
        return CONTEXT.get();
    }

    /**
     * Get current user ID
     */
    public static Long getCurrentUserId() {
        UserInfo userInfo = CONTEXT.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * Get current username
     */
    public static String getCurrentUsername() {
        UserInfo userInfo = CONTEXT.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * Clear context
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
