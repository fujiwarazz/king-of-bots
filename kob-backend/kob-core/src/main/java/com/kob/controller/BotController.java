package com.kob.controller;


import com.kob.model.dto.BotAddDto;
import com.kob.model.dto.BotUpdateDto;
import com.kob.service.IBotService;
import com.kob.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-07
 */
@RestController
@RequestMapping("/bot")
public class BotController {

    @Resource
    private IBotService botService;

    @PostMapping("/add")
    public ResponseResult<?> addBot(@RequestBody BotAddDto botAddDto){
        return botService.addBot(botAddDto);
    }

    @PostMapping("/deleteBot/{id}")
    public ResponseResult<?> deleteBot(@PathVariable Long id){
        return botService.removeBot(id);
    }

    @PostMapping("/updateBot")
    public ResponseResult<?>updateBot(@RequestBody BotUpdateDto botUpdateDto){
        return botService.updateBot(botUpdateDto);
    }
    @GetMapping("/getList/{id}")
    public ResponseResult<?>getMyBots(@PathVariable Long id){
        return botService.getMyBots(id);
    }

}

