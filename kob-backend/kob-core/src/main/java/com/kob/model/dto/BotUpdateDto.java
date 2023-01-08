package com.kob.model.dto;

import com.kob.model.entity.Bot;
import lombok.Data;

/**
 * @Author peelsannaw
 * @create 07/01/2023 18:20
 */
@Data
public class BotUpdateDto   {
    private Long id;
    private String description;
    private String code;
    private String title;
    private Boolean isOpen;
    private Long userId;
}
