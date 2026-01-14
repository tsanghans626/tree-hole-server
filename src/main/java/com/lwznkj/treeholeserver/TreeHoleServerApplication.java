package com.lwznkj.treeholeserver;

import com.tangzc.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoTable
@SpringBootApplication
@MapperScan("com.lwznkj.treeholeserver.module.*.mapper")
public class TreeHoleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreeHoleServerApplication.class, args);
	}

}
