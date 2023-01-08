package com.kob.service.impl;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kob.annotation.Prevent;
import com.kob.model.dto.BotAddDto;
import com.kob.model.dto.BotUpdateDto;
import com.kob.model.entity.Bot;
import com.kob.mapper.BotMapper;
import com.kob.service.IBotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.util.AppHttpCodeEnum;
import com.kob.util.ResponseResult;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-07
 */
@Service
public class BotServiceImpl extends ServiceImpl<BotMapper, Bot> implements IBotService {

    @Resource
    private BotMapper botMapper;

    @Override
    @Transactional(rollbackFor = MysqlDataTruncation.class)
    public ResponseResult<?> addBot(BotAddDto botAddDto) {
        System.out.println(botAddDto);
        if(StrUtil.isBlank(botAddDto.getCode()) ){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"代码不为空!");
        }
        if(StrUtil.isBlank(botAddDto.getTitle())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"标题不为空!");
        }
        try {
            if( StrUtil.isBlank(botAddDto.getDescription())){
                botAddDto.setDescription("这个人很懒,连描述也不写~");
            }
            Bot bot = new Bot();
            bot.setBTitle(botAddDto.getTitle());
            bot.setBDesc(botAddDto.getDescription());
            bot.setBCode(botAddDto.getCode());
            bot.setKId(StpUtil.getLoginIdAsLong());

            bot.setBIsOpen(botAddDto.getIsOpen().equals(Boolean.TRUE)?1:0);

            this.save(bot);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"请检查参数是否过长!");
        }
    }

    @Override
    public ResponseResult<?> removeBot(Long id) {
        if(id==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Bot byId = this.getById(id);
        if(byId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"没有这个Bot!");
        }
        if(!byId.getKId().equals(StpUtil.getLoginIdAsLong())){
            return ResponseResult.errorResult(114514,"这不是您的bot!");
        }else{
            this.removeById(id);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult<?> getInfoOfBot() {
        return null;
    }

    @Override
//    @Prevent("30")
    @Transactional(rollbackFor = MysqlDataTruncation.class)
    public ResponseResult<?> updateBot(BotUpdateDto botUpdateDto) {
        try {
            if(StrUtil.isBlank(botUpdateDto.getTitle()) ||StrUtil.isBlank(botUpdateDto.getCode())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            if(this.getById(botUpdateDto.getId())==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"没有这个Bot!");
            }
            if(!botUpdateDto.getUserId().equals(StpUtil.getLoginIdAsLong())){
                return ResponseResult.errorResult(114514,"这不是您的bot!");
            }
            Bot bot = new Bot();
            bot.setBIsOpen(botUpdateDto.getIsOpen().equals(Boolean.TRUE)?1:0);
            bot.setKId(botUpdateDto.getUserId());
            bot.setBId(botUpdateDto.getId());
            bot.setBDesc(botUpdateDto.getDescription());
            bot.setBCode(botUpdateDto.getCode());
            bot.setBTitle(botUpdateDto.getTitle());
            this.updateById(bot);
            return ResponseResult.okResult();
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"请检查参数是否过长!");
        }
    }

    @Override
    public ResponseResult<?> getMyBots(Long id) {
        if(id==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(id!= StpUtil.getLoginIdAsLong()){
            return ResponseResult.errorResult(1919810,"你小子想干啥?");
        }
        List<Bot> list = this.list(Wrappers.lambdaQuery(Bot.class).eq(Bot::getKId, id));
        list.forEach(item->{
            item.setIsOpen(item.getBIsOpen() == 1);
        });
        return ResponseResult.okResult(list);
    }
}
