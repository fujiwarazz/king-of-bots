package com.kob.service;


import com.kob.model.ResponseResult;

/**
* @Author peelsannaw
* @create 09/01/2023 20:15
*/
public interface MatchingService {

    ResponseResult<?> addPlayer(Long userId, Integer rating);

    ResponseResult<?>removePlayer(Long userId);
}
