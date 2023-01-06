package com.kob.model.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @Author peelsannaw
 * @create 06/01/2023 19:04
 */
@Data
public class UserLoginDto {

    private String nickname;
    private String password;
    private String email;
    private String code;
}
