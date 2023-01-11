package com.kob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.mapper.UserMapper;
import com.kob.model.dto.RatingPageDto;
import com.kob.model.entity.User;
import com.kob.model.vo.PageVo;
import com.kob.service.RatingService;
import com.kob.util.ResponseResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author peelsannaw
 * @create 10/01/2023 23:40
 */
@Service
public class RatingServiceImpl implements RatingService {
    @Resource
    private UserMapper userMapper;

    @Override
    @SuppressWarnings("all")
    public ResponseResult<?> getRatingList(RatingPageDto pageDto) {
        LambdaQueryWrapper<User>lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(pageDto.getNickname()!=null){
            lambdaQueryWrapper.eq(User::getNickname,pageDto.getNickname());
        }
        lambdaQueryWrapper.select(User::getNickname,User::getRating,User::getAvatar,User::getId,User::getColor).orderByDesc(User::getRating);
        Page<User> page = userMapper.selectPage(new Page<>(pageDto.getPageNum(),10), lambdaQueryWrapper);
        page.getRecords();
        PageVo<User> userPageVo = new PageVo<>();
        userPageVo.setData(page.getRecords());
        userPageVo.setTotal((int) page.getTotal());
        userPageVo.setCurrent(pageDto.getPageNum());
        return ResponseResult.okResult(userPageVo);
    }
}
