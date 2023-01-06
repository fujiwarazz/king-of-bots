package com.kob.util;

import com.kob.model.entity.User;

/**
 * @Author peelsannaw
 * @create 06/01/2023 16:13
 */
public class UserContextHolder {
    private static final ThreadLocal<User> UserThreadLocal = new ThreadLocal<>();


    public static void setUser(User user){
        UserThreadLocal.set(user);
    }

    public static void remove(){
        UserThreadLocal.remove();
    }
    public static User getUserInfo(){
        return UserThreadLocal.get();
    }
}
