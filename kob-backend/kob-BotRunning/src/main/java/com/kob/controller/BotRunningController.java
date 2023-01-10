package com.kob.controller;

import com.kob.model.ResponseResult;
import com.kob.model.dto.BotRunningDto;
import com.kob.service.BotRunningService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author peelsannaw
 * @create 10/01/2023 10:29
 */
@RestController
public class BotRunningController {

    @Resource
    private BotRunningService botRunningService;

    @PostMapping("/bot/running/add")
    public ResponseResult<?> addBotRunning(@RequestBody  BotRunningDto botRunningDto){
        return botRunningService.addBotRunning(botRunningDto);
    }
}
