package com.kob.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.model.dto.UserLoginDto;
import com.kob.model.dto.UserRegDto;
import com.kob.model.entity.User;
import com.kob.service.IUserService;
import com.kob.util.Base64Utils;
import com.kob.util.ResponseResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Author peelsannaw
 * @create 06/01/2023 11:15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    @PostMapping("/account/register")
    public ResponseResult<?> register(@RequestBody UserRegDto userRegDto){
        return userService.handleRegister(userRegDto);
    }

    @PostMapping("/account/avatar")
    public ResponseResult<?> uploadAvatar(@RequestParam("avatar") MultipartFile file){
        return userService.uploadUserAvatar(file);
    }

    @PostMapping("/account/token")
    public ResponseResult<?> getTokenInfo(){
        return ResponseResult.okResult();
    }

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request){
        return userService.handleLogin(userLoginDto,request);
    }

    @GetMapping("/account/info")
    public ResponseResult<?> getInfoByToken(){
        return userService.getInfoByToken();
    }

    @PostMapping("/account/logout")
    public ResponseResult<?> logout(){
        StpUtil.logout();
        return ResponseResult.okResult();
    }
}
