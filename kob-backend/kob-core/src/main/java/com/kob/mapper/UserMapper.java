package com.kob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.kob.model.dto.RecordsPageDto;
import com.kob.model.entity.Records;
import com.kob.model.entity.User;
import com.kob.model.vo.RecordsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author peelsannaw
 * @create 06/01/2023 12:51
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<RecordsVo> getList(RecordsPageDto pageDto);
}
