package com.kob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.model.entity.User;

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
}
