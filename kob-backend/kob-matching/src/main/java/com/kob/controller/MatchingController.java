package com.kob.controller;

import com.alibaba.fastjson.JSON;
import com.kob.MatchClient;
import com.kob.model.ResponseResult;
import com.kob.model.dto.PlayerMatchDto;
import com.kob.service.MatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author peelsannaw
 * @create 09/01/2023 20:18
 */
@RestController
@Slf4j
@RequestMapping("/match")
public class MatchingController implements MatchClient {

    @Resource
    private MatchingService matchingService;


    @Override
    @PostMapping("/player/add")
    public ResponseResult<?> addPlayer(PlayerMatchDto playerMatchDto) {
        log.info("接受feign调用:[{}]", JSON.toJSONString(playerMatchDto));
        return matchingService.addPlayer(playerMatchDto.getUserId(), playerMatchDto.getRating());
    }

    @Override
    @PostMapping("/player/remove")
    public ResponseResult<?> removePlayer(PlayerMatchDto playerMatchDto) {
        log.info("接受feign调用:[{}]", JSON.toJSONString(playerMatchDto));
        return matchingService.removePlayer(playerMatchDto.getUserId());
    }
}
