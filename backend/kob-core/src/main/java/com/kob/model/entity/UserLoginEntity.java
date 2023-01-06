package com.kob.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author peelsannaw
 * @create 06/01/2023 12:53
 */
@Getter
@Setter
@TableName("s_user_login")
public class UserLoginEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 登录时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * ip
     */
    @TableField("ip")
    private Integer ip;

    /**
     * 国家
     */
    @TableField("country")
    private String country;

    /**
     * 地区
     */
    @TableField("area")
    private String area;


}
