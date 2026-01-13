package com.lwznkj.treeholeserver.module.users.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwznkj.treeholeserver.module.users.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
