package com.kob.config.stp;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kob.interceptor.StpLoginInterceptor;
import com.kob.util.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * @Author peelsannaw
 * @create 06/01/2023 13:01
 */
@Configuration
@Slf4j
public class StpApiConfig implements WebMvcConfigurer {
    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter();
    }

    @Value("${spring.jackson.date-format}")
    private String dateFormatPattern;

    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();
        // ??????JSON???,?????????Long?????????String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        // ???????????????
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //??????????????????spring boot yml ????????????????????????????????????
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormatPattern));
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        // ?????????????????????
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);


    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //??????????????????
        registry.addMapping("/**")
                //????????????Cookie
                .allowCredentials(true)
                //???????????????????????????   SpringBoot2.4.4??????????????????.allowedOrigins("*")
                .allowedOrigins("*")
                //????????????????????????
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                //.allowedMethods("*") //??????????????????
                //????????????????????????????????????
                .allowedHeaders("*")
                //????????????????????????????????????
                .exposedHeaders("*");
    }
    @Resource
    StpLoginInterceptor stpLoginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(stpLoginInterceptor).addPathPatterns("/**").excludePathPatterns("/ws/**","/combat/start");
        // ?????????????????????????????????????????????
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.match(SaHttpMethod.OPTIONS).match("/**").stop();
            SaRouter.match("/ws/**").stop();
            SaRouter.match("/combat/**").stop();
            SaRouter.match("/**", r -> {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                assert requestAttributes != null;
                HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
                assert request != null;
                log.info("????????????url:[{}]",request.getRequestURL());
                log.info("????????????token:[{}]",request.getHeader("kob_token"));
            });

            SaRouter
                    .match("/**")    // ????????? path ???????????????????????? */
                    .notMatch("/user/login","/user/account/register","/user/account/avatar")        // ???????????? path ????????????????????????
                    .check(r -> StpUtil.checkLogin());

//            // ???????????? -- ????????? admin ?????????????????????????????? admin ???????????? super-admin ???????????????????????????
//            SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
//
//            // ???????????? -- ??????????????????????????????
//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));



        })).addPathPatterns("/**");
    }
}

