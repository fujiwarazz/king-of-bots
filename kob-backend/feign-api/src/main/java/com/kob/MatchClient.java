package com.kob;

import com.kob.model.ResponseResult;
import com.kob.model.dto.PlayerMatchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author peelsannaw
 * @create 09/01/2023 21:04
 */
@Component
@FeignClient("matching-service")
public interface MatchClient {

    @PostMapping("/match/player/add")
     ResponseResult<?> addPlayer(@RequestBody PlayerMatchDto playerMatchDto);

    @PostMapping("/match/player/remove")
     ResponseResult<?> removePlayer(@RequestBody PlayerMatchDto playerMatchDto);
}
