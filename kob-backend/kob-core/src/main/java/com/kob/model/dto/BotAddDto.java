package com.kob.model.dto;

import com.kob.model.entity.Bot;
import io.swagger.models.auth.In;
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
