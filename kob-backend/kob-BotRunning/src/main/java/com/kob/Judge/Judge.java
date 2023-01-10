package com.kob.Judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peelsannaw
 * @create 10/01/2023 14:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Judge {
    Long userId;
    String botCode;
    String input;
}
