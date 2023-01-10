package com.kob.config;

import com.kob.interceptor.MatchingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author peelsannaw
 * @create 09/01/2023 20:28
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    MatchingInterceptor matchingInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(matchingInterceptor)
                .addPathPatterns("/**");
    }
}
