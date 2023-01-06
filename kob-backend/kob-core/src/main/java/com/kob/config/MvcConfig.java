package com.kob.config;

import com.kob.util.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author peelsannaw
 * @create 06/01/2023 17:01
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public HttpMessageConverter h() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        return mappingJackson2HttpMessageConverter;
    }
}
