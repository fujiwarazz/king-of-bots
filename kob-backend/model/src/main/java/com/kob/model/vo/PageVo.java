package com.kob.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author peelsannaw
 * @create 10/01/2023 18:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo<T> {
    List<T> data;
    Integer total;
    Integer current;
}
