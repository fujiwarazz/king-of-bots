package com.kob.model.dto;

import lombok.Data;

/**
 * @Author peelsannaw
 * @create 09/01/2023 20:20
 */
@Data
public class PlayerMatchDto {

    private Long userId;
    private Integer rating;
    private Long bId;
    private Integer matchType;
}

