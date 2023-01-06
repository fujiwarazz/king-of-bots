package com.kob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author peelsannaw
 * @create 06/01/2023 12:51
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
