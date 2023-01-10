package com.kob.service;

import com.kob.model.ResponseResult;
import com.kob.model.dto.BotRunningDto;

/**
 * @Author peelsannaw
 * @create 10/01/2023 10:26
 */
public interface BotRunningService {

    /**
     * 编译代码
     * @param botRunningDto
     * @return
     */
    ResponseResult<?> addBotRunning(BotRunningDto botRunningDto);
}
