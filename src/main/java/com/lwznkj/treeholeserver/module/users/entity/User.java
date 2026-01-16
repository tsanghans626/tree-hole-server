package com.lwznkj.treeholeserver.module.users.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.mpe.autotable.annotation.*;

import com.tangzc.mpe.base.entity.BaseLogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table("users")
public class User extends BaseLogicEntity<Long, LocalDateTime> {

    @ColumnId(value = "id", mode = IdType.AUTO)
    private Long id;

    @ColumnComment("用户名")
    @Column(value = "username")
    private String username;

    @ColumnComment("昵称")
    @Column(value = "name")
    private String name;
}
