package com.kob.model.dto;


import lombok.Data;

/**
 * @Author peelsannaw
 * @create 06/01/2023 18:54
 */
@Data
public class UserRegDto  {
   private String username;
   private String password;
   private String email;
   private String avatar;
    public boolean checkEmail(){
        String reg = "\\w{1,}@(\\w+\\.)+\\w+";
        return this.email.matches(reg);
    }

}

