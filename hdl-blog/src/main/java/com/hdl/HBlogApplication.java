package com.hdl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */
@SpringBootApplication()
@MapperScan("com.hdl.mapper")
public class HBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(HBlogApplication.class, args);
    }
}
