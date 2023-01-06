package com.kob.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.annotation.Prevent;
import com.kob.controller.UserController;
import com.kob.mapper.UserMapper;
import com.kob.model.dto.UserLoginDto;
import com.kob.model.dto.UserRegDto;
import com.kob.model.entity.User;
import com.kob.model.vo.UserInfoVo;
import com.kob.service.IUserService;

import com.kob.util.AppHttpCodeEnum;
import com.kob.util.Base64Utils;
import com.kob.util.ResponseResult;
import com.kob.util.UserContextHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Wrapper;
import java.util.Base64;
import java.util.Locale;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;
    /**
     * 记录登录信息
     * @param loginIdAsInt 用户id
     */
    @Override
    public void logLogin(int loginIdAsInt) {

    }

    @Override
//    @Prevent(value = "3")
    public ResponseResult<?> handleLogin(UserLoginDto userLoginDto, HttpServletRequest request) {
        try {
            if(StpUtil.isLogin()){
                StpUtil.logout();
            }
            String nickname = userLoginDto.getNickname();
            String loginDtoPassword = userLoginDto.getPassword();
            if(nickname == null || loginDtoPassword == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            String encryptPassword =Base64Utils.encode((loginDtoPassword+Base64Utils.DEFAULT_SALT).getBytes(StandardCharsets.UTF_8));
            User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getNickname, nickname));
            if(user==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST.getCode(),"无此用户!");
            }
            if(!user.getPassword().equals(encryptPassword)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            UserContextHolder.setUser(user);
            String userAgent = request.getHeader("user-agent").toLowerCase(Locale.ROOT);
            String type = StpUtil.getLoginType();
            StpUtil.login(user.getId(),type);
            System.out.println(StpUtil.getLoginType());
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setUserId(user.getId());
            userInfoVo.setToken(StpUtil.getTokenValue());
            userInfoVo.setNickname(user.getNickname());
            userInfoVo.setAvatar(user.getAvatar());
            return ResponseResult.okResult(userInfoVo);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseResult<?> handleRegister(UserRegDto userRegDto) {
        String nickname = userRegDto.getNickname();
        String password = userRegDto.getPassword();
        String email = userRegDto.getEmail();
        if(nickname==null || password==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(password);
        if(userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getNickname,nickname))!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"此用户已存在!");
        }else{
            this.save(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getInfoByToken() {
        UserInfoVo userInfoVo = new UserInfoVo();
        User loginUser = UserContextHolder.getUserInfo();
        BeanUtil.copyProperties(loginUser,userInfoVo);
        return ResponseResult.okResult(userInfoVo);
    }
}
