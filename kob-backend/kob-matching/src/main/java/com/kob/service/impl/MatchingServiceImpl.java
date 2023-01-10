package com.kob.service.impl;

import com.kob.model.Player;
import com.kob.model.ResponseResult;
import com.kob.service.MatchingService;
import com.kob.util.MatchingPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author peelsannaw
 * @create 09/01/2023 20:15
 */
@Service
@Slf4j
public class MatchingServiceImpl implements MatchingService {

    public final static MatchingPool MATCHING_POOL = new MatchingPool();

    @Override
    public ResponseResult<?> addPlayer(Long userId, Integer rating, Long bid, Integer matchType) {
        log.info("添加用户");
        MATCHING_POOL.addPlayer(new Player(userId,rating,0,bid,matchType));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> removePlayer(Long userId) {
        log.info("移除用户");
        MATCHING_POOL.removePlayer(userId);
        return ResponseResult.okResult();
    }
}
