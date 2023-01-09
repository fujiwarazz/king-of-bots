package com.kob.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * @Author peelsannaw
 * @create 09/01/2023 22:03
 */
@Configuration
public class RestTemplateConfig {
    @Value("${spring.jackson.date-format}")
    private String dateFormatPattern;

    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    @Bean
    public RestTemplate restTemplate(){

        RestTemplate restTemplate = new RestTemplate();

//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = converter.getObjectMapper();
//        // 生成JSON时,将所有Long转换成String
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        // 时间格式化
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        //这个可以引用spring boot yml 里的格式化配置和时区配置
//        objectMapper.setDateFormat(new SimpleDateFormat(dateFormatPattern));
//        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
//        // 设置格式化内容
//        converter.setObjectMapper(objectMapper);
//        ArrayList<HttpMessageConverter<?>> objects = new ArrayList<>();
//        objects.add(converter);
//        restTemplate.setMessageConverters(objects);
        return restTemplate;
    }
}
