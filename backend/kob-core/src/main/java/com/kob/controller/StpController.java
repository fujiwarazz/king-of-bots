package com.kob.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;

/**
 * @Author peelsannaw
 * @create 06/01/2023 13:14
 */

@RestController
public class StpController {

    @GetMapping("/login")
    public String login(){
        StpUtil.login("123","PC");
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return JSON.toJSONString(tokenInfo);
    }
    @GetMapping("/isLogin")
    public Boolean isLogin(){

        return StpUtil.isLogin();
    }
    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }


}
