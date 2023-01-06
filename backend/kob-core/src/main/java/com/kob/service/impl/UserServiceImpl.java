package com.kob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.mapper.UserMapper;
import com.kob.model.entity.User;
import com.kob.service.IUserService;

import org.springframework.stereotype.Service;

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

    /**
     * 记录登录信息
     * @param loginIdAsInt 用户id
     */
    @Override
    public void logLogin(int loginIdAsInt) {


    }
}
