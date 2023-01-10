package com.kob.service.impl;

import com.kob.Judge.JudgingPool;
import com.kob.model.ResponseResult;
import com.kob.model.dto.BotRunningDto;
import com.kob.service.BotRunningService;
import org.springframework.stereotype.Service;

/**
 * @Author peelsannaw
 * @create 10/01/2023 10:26
 */
@Service
public class BotRunningServiceImpl implements com.kob.service.BotRunningService {

    public static final JudgingPool JUDGING_POOL = new JudgingPool();
    @Override
    public ResponseResult<?> addBotRunning(BotRunningDto botRunningDto) {
        JUDGING_POOL.addOneJudge(botRunningDto.getUserId(), botRunningDto.getBotCode(), botRunningDto.getInput());
        return ResponseResult.okResult();
    }
}
