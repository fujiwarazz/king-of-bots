package com.kob.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author peelsannaw
 * @create 04/01/2023 19:24
 */
@SpringBootApplication
@Slf4j
public class KobApplication {
    public static void main(String[] args) {
        SpringApplication.run(KobApplication.class,args);
        log.info("kobApplication running success");

    }
}
