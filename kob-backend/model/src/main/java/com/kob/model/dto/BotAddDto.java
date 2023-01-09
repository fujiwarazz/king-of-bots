package com.kob.model.dto;

import lombok.Data;

/**
 * @Author peelsannaw
 * @create 07/01/2023 16:39
 */
@Data
public class BotAddDto {

    private String description;
    private String code;
    private String title;
    private Boolean isOpen;

}
