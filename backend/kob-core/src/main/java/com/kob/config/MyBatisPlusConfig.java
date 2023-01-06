package com.kob.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author peelsannaw
 * @create 06/01/2023 13:23
 */
@Configuration
@MapperScan("com.kob.mapper")
public class MyBatisPlusConfig {


}
