package com.kob.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peelsannaw
 * @create 09/01/2023 22:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long userId;
    private Integer rating;
    private Integer waitingTime;
    private Long bid;
    private Integer matchType;
}
