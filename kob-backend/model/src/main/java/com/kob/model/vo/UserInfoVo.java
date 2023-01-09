package com.kob.model.vo;

import lombok.Data;

/**
 * @Author peelsannaw
 * @create 06/01/2023 19:38
 */
@Data
public class UserInfoVo {

    private String nickname;
    private String avatar;
    private Long userId;
    private String token;
}
