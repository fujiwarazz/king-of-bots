package com.kob.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author peelsannaw
 * @create 06/01/2023 12:48
 */
@Slf4j
@Configuration
public class StpLoginInterceptor implements HandlerInterceptor {
    @Value("${sa-token.timeout}")
    private Integer renewTimeout;

    @Resource
    private IUserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("Token:" + StpUtil.getTokenInfo());
        try {
            // 若在长期有效期内，则自动续签临时和长久有效期
            if (StpUtil.getTokenTimeout() > 0) {
                // 长时间无操作，视为重新登录
                if (StpUtil.getTokenActivityTimeout() < 0) {
                    log.debug("Request re login, Log login");
                    int id = Integer.parseInt(String.valueOf(StpUtil.getLoginIdByToken(StpUtil.getTokenValue())));
                    userService.logLogin(id);
                }
                // 续签临时有效期
                StpUtil.updateLastActivityToNow();
                // 续签长时有效期
                StpUtil.renewTimeout(renewTimeout);
                log.debug("Request is Login, refresh activity token");
            }
        } catch (Exception ignore) {
        }
        return true;
    }
}
