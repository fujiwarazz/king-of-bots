package com.kob.config.stp;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author peelsannaw
 * @create 06/01/2023 13:06
 */
@Component
public class StpServiceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return null;
    }
}
