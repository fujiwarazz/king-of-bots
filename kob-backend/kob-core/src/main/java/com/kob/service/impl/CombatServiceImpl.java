package com.kob.service.impl;

import com.kob.service.ICombatService;
import com.kob.websocket.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kob.constant.GameConstant.START_GAME_SUCCESS;

/**
 * @Author peelsannaw
 * @create 09/01/2023 23:34
 */
@Service
@Slf4j
public class CombatServiceImpl implements ICombatService {


    @Override
    public String startGame(Long userId1, Long userId2) {
        log.info("接收到信息,两名玩家id为:[{}]以及[{}]",userId1,userId2);
        Consumer.startGame(userId1,userId2);
        return START_GAME_SUCCESS;
    }
}
