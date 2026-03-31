package com.horseracing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 * MapperScan 扫描所有 Mapper 接口
 */
@SpringBootApplication
@MapperScan("com.horseracing.mapper")
public class HorseRacingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HorseRacingApplication.class, args);
    }
}
