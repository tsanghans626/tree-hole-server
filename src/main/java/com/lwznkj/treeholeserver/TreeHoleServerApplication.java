package com.lwznkj.treeholeserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lwznkj.treeholeserver.module.*.mapper")
public class TreeHoleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreeHoleServerApplication.class, args);
	}

}
