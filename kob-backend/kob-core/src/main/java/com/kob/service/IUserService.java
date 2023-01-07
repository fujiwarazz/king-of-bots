package com.kob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.model.dto.UserLoginDto;
import com.kob.model.dto.UserRegDto;
import com.kob.model.entity.User;
import com.kob.util.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-06
 */
public interface IUserService extends IService<User> {


    void logLogin(int loginIdAsInt);

    ResponseResult<?> handleLogin(UserLoginDto userLoginDto, HttpServletRequest request);

    ResponseResult<?> handleRegister(UserRegDto userRegDto);

    ResponseResult<?> getInfoByToken();

    ResponseResult<?> uploadUserAvatar(MultipartFile file);
}
