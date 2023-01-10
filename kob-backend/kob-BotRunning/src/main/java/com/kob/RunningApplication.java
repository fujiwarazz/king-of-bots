package com.kob;

import com.kob.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author peelsannaw
 * @create 10/01/2023 10:24
 */
@SpringBootApplication
public class RunningApplication {
    public static void main(String[] args) {

        BotRunningServiceImpl.JUDGING_POOL.start();
        SpringApplication.run(RunningApplication.class,args);

    }
}
