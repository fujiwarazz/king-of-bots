package com.kob.service.impl;

import com.kob.gamecore.Game;
import com.kob.service.ReceiveBotMovingService;
import com.kob.util.ResponseResult;
import com.kob.websocket.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author peelsannaw
 * @create 10/01/2023 15:21
 */
@Service
@Slf4j
public class ReceiveBotMovingServiceImpl implements ReceiveBotMovingService {


    @Override
    public ResponseResult<?> receiveBotMove(Long userId, Integer direction) {
        log.info("接受调用:userId:[{}],方向:[{}]", userId, direction);
        if (Consumer.USERS_LINK.get(userId) != null) {
            Game game = Consumer.USERS_LINK.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    //bot输入
                    game.setNextStepA(direction);

                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }
        return ResponseResult.okResult();
    }
}
