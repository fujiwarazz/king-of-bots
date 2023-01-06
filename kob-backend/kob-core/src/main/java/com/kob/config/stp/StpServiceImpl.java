package com.kob.config.stp;

import cn.dev33.satoken.stp.StpInterface;
import com.kob.mapper.UserMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author peelsannaw
 * @create 06/01/2023 13:06
 */
@Component
public class StpServiceImpl implements StpInterface {

    @Resource
    private UserMapper userMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("user");
        return roles;
    }


    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("user");
        return roles;
    }
}
