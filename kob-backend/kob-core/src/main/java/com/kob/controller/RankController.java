package com.kob.controller;

import com.kob.model.dto.RatingPageDto;
import com.kob.service.RatingService;
import com.kob.util.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author peelsannaw
 * @create 06/01/2023 11:15
 */
@RestController
public class RankController {

    @Resource
    private RatingService ratingService;

    @PostMapping("/rating/list")
    public ResponseResult<?> getRankList(@RequestBody RatingPageDto pageDto){
        return ratingService.getRatingList(pageDto);
    }

}
