package com.kob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.mapper.UserMapper;
import com.kob.model.dto.RecordsPageDto;
import com.kob.model.entity.Records;
import com.kob.mapper.RecordsMapper;
import com.kob.model.entity.User;
import com.kob.model.vo.PageVo;
import com.kob.model.vo.RecordsVo;
import com.kob.service.IRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.util.AppHttpCodeEnum;
import com.kob.util.ResponseResult;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 对局记录 服务实现类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-09
 */
@Service
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Records> implements IRecordsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult<?> getRecordsPage(RecordsPageDto pageDto) {
        PageVo<RecordsVo> pageVo = null;
        try {
            LambdaQueryWrapper<Records>lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if(pageDto.getNickname()!=null){
                User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getNickname, pageDto.getNickname()));
                if(user==null){
                    return ResponseResult.okResult(new PageVo<>());
                }
                lambdaQueryWrapper.eq(Records::getAId,user.getId()).or().eq(Records::getBId,user.getId());
            }
            lambdaQueryWrapper.orderByDesc(Records::getCreateTime);
            Page<Records> page = this.page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()), lambdaQueryWrapper);
            List<Records> records = page.getRecords();
            List<RecordsVo> result = records.stream().map(item -> {
                User playerA = userMapper.selectById(item.getAId());
                User playerB = userMapper.selectById(item.getBId());
                RecordsVo recordsVo = BeanUtil.copyProperties(item, RecordsVo.class);
                recordsVo.setAAvatar(playerA.getAvatar());
                recordsVo.setAUsername(playerA.getNickname());
                recordsVo.setBAvatar(playerB.getAvatar());
                recordsVo.setBUserName(playerB.getNickname());
                if("A".equals(item.getLoser())){
                    recordsVo.setWinnerNickname(playerB.getNickname()+"胜");
                    recordsVo.setLoserNickname(playerA.getNickname());
                }else if("B".equals(item.getLoser())){
                    recordsVo.setWinnerNickname(playerA.getNickname()+"胜");
                    recordsVo.setLoserNickname(playerB.getNickname());
                }else{
                    recordsVo.setWinnerNickname("平局");
                }
                return recordsVo;
            }).collect(Collectors.toList());
            pageVo = new PageVo<>();
            pageVo.setData(result);
            pageVo.setCurrent((int) page.getCurrent());
            pageVo.setTotal((int) page.getTotal());
            return ResponseResult.okResult(pageVo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
    }
}
