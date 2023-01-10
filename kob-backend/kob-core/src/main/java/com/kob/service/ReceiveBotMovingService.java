package com.kob.service;

import com.kob.util.ResponseResult;

/**
 * @Author peelsannaw
 * @create 10/01/2023 15:20
 */
public interface ReceiveBotMovingService {

    /**
     * 接受编译后代码的执行方向
     * @param userId
     * @param direction
     * @return
     */
    ResponseResult<?>receiveBotMove(Long userId,Integer direction);
}
