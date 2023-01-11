package com.kob.service;

import com.kob.model.dto.RatingPageDto;
import com.kob.util.ResponseResult;

/**
 * @Author peelsannaw
 * @create 10/01/2023 23:37
 */
public interface RatingService {

    ResponseResult<?> getRatingList(RatingPageDto pageDto);
}
