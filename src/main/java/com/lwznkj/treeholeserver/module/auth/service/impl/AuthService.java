package com.lwznkj.treeholeserver.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lwznkj.treeholeserver.module.auth.dto.TokenResp;
import com.lwznkj.treeholeserver.module.auth.service.IAuthService;
import com.lwznkj.treeholeserver.module.auth.util.JwtUtil;
import com.lwznkj.treeholeserver.module.users.entity.User;
import com.lwznkj.treeholeserver.module.users.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public TokenResp debugLogin(String username) {
        // Find or create user
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            User newUser = new User();
            newUser.setUsername(username);
            userMapper.insert(newUser);
            user = newUser;
        }

        // Generate token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new TokenResp(token, "Bearer", user.getId(), user.getUsername());
    }
}
