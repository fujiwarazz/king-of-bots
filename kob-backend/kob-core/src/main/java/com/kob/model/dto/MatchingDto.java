package com.kob.model.dto;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;
import lombok.Getter;

/**
 * @Author peelsannaw
 * @create 08/01/2023 14:55
 */
@Data
public class MatchingDto {
    String event;
    Integer direction;

    @SuppressWarnings("all")
    @Getter
    public enum event{
        START("start-matching"),
        END("end-matching");
        @EnumValue
        String name;

        event(String name) {
            this.name = name;
        }
    }
}
