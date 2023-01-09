package com.kob;

import com.kob.service.MatchingService;
import com.kob.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.swing.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author peelsannaw
 * @create 08/01/2023 09:50
 */
@EnableFeignClients
@SpringBootApplication
public class MatchingApplication {

    private static ExecutorService executorService = Executors.newFixedThreadPool(100);
    public static void main(String[] args) {
        SpringApplication.run(MatchingApplication.class,args);

        for (int i = 0;i<10;i++){
            executorService.submit(MatchingServiceImpl.MATCHING_POOL::start);
        }

    }
}
