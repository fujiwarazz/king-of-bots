package com.kob.model.dto;

import lombok.Data;

/**
 * @Author peelsannaw
 * @create 10/01/2023 10:27
 */
@Data
public class BotRunningDto {
    Long userId;
    String botCode;
    String input;
}
