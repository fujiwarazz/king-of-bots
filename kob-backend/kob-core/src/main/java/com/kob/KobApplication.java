package com.kob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author peelsannaw
 * @create 06/01/2023 16:01
 */
@EnableFeignClients
@SpringBootApplication
public class KobApplication {
    public static void main(String[] args) {
        SpringApplication.run(KobApplication.class,args);

    }
}
