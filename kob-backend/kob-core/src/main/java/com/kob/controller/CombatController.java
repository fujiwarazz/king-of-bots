package com.kob.controller;

import com.kob.util.ResponseResult;
import com.kob.model.dto.BotMovingDto;
import com.kob.model.dto.PlayerMatchDto;
import com.kob.model.dto.PlayerMatchSuccessDto;
import com.kob.service.ICombatService;
import com.kob.service.ReceiveBotMovingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author peelsannaw
 * @create 06/01/2023 11:16
 */
@RestController
@Slf4j
public class CombatController {
    @Resource
    private ICombatService combatService;
    @Resource
    private ReceiveBotMovingService receiveBotMovingService;
    @PostMapping("/combat/start")
    public String startGame(@RequestBody  MultiValueMap<String,String> multiValueMap){
        Long aId = Long.parseLong(Objects.requireNonNull(multiValueMap.getFirst("a_id")));
        Long bId = Long.parseLong(Objects.requireNonNull(multiValueMap.getFirst("b_id")));
        Long abId = Long.parseLong(Objects.requireNonNull(multiValueMap.getFirst("a_bid")));
        Long bbId = Long.parseLong(Objects.requireNonNull(multiValueMap.getFirst("b_bid")));
        log.info("接受远程调用:a:[{}][{}],b:[{}][{}]",aId,bId,abId,bbId);
        return combatService.startGame(aId,bId,abId,bbId);
    }

    @PostMapping("/combat/receive")
    public ResponseResult<?> receiveMove(@RequestBody BotMovingDto botMovingDto){
        return receiveBotMovingService.receiveBotMove(botMovingDto.getUserId(), botMovingDto.getDirection());
    }

}
