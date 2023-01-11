package com.kob.model.dto;

import lombok.Data;

/**
 * @Author peelsannaw
 * @create 10/01/2023 18:08
 */
@Data
public class RecordsPageDto {
    private Long pageSize;
    private Long pageNum;
    private String nickname;
}
