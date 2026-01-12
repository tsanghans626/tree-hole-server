package com.lwznkj.treeholeserver.module.auth.service.impl;

import com.lwznkj.treeholeserver.module.auth.dto.TokenResp;
import com.lwznkj.treeholeserver.module.auth.service.IAuthService;
import com.lwznkj.treeholeserver.module.auth.util.JwtUtil;
import com.lwznkj.treeholeserver.module.users.entity.User;
import com.lwznkj.treeholeserver.module.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public TokenResp debugLogin(String username) {
        // Find or create user
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    return userRepository.save(newUser);
                });

        // Generate token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new TokenResp(token, "Bearer", user.getId(), user.getUsername());
    }
}
