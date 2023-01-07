package com.kob.service;

import com.kob.model.dto.BotAddDto;
import com.kob.model.dto.BotUpdateDto;
import com.kob.model.entity.Bot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.util.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-07
 */
public interface IBotService extends IService<Bot> {

    /**
     * 新增
     * @return
     */
    ResponseResult<?>addBot(BotAddDto botAddDto);
    /**
     * 新增
     * @return
     */
    ResponseResult<?>removeBot(Long id);
    /**
     * 新增
     * @return
     */
    ResponseResult<?>getInfoOfBot();
    /**
     * 新增
     * @return
     */
    ResponseResult<?>updateBot(BotUpdateDto botUpdateDto);

    ResponseResult<?> getMyBots(Long id);
}
