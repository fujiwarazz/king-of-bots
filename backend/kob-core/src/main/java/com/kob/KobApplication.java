package com.kob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author peelsannaw
 * @create 04/01/2023 20:29
 */
@SpringBootApplication
@Slf4j
public class KobApplication {

    public static void main(String[] args) {
        SpringApplication.run(KobApplication.class,args);
    }
}

