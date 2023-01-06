package com.kob.model.dto;

import com.kob.model.entity.User;
import lombok.Data;

/**
 * @Author peelsannaw
 * @create 06/01/2023 18:54
 */
@Data
public class UserRegDto  {
    String nickname;
    String password;
    String email;

    public boolean checkEmail(){
        String reg = "\\w{1,}@(\\w+\\.)+\\w+";
        return this.email.matches(reg);
    }

}

