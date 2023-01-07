package com.kob.config.stp;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.kob.interceptor.StpLoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author peelsannaw
 * @create 06/01/2023 13:01
 */
@Configuration
@Slf4j
public class StpApiConfig implements WebMvcConfigurer {

    @Resource
    StpLoginInterceptor stpLoginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(stpLoginInterceptor).addPathPatterns("/**");
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.match(SaHttpMethod.OPTIONS).stop();

            SaRouter.match("/**", r -> {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
                assert request != null;
                log.info("当前请求url:[{}]",request.getRequestURL());
                log.info("当前请求token:[{}]",request.getHeader("kob-token"));
            });

            SaRouter
                    .match("/**")    // 拦截的 path 列表，可以写多个 */
                    .notMatch("/user/login","/user/account/register")        // 排除掉的 path 列表，可以写多个
                    .check(r -> StpUtil.checkLogin());

            // 角色校验 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
            SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));

            // 权限校验 -- 不同模块校验不同权限
            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));

            // 甚至你可以随意的写一个打印语句


            // 连缀写法
            SaRouter.match("/**").check(r -> System.out.println("----啦啦啦----"));

        })).addPathPatterns("/**");
    }
}

// 根据路由划分模块，不同模块不同鉴权
//            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));