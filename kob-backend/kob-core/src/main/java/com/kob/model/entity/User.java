package com.kob.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-06
 */
@Getter
@Setter
@TableName("k_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户主键")
    @TableId(value = "k_id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户昵称")
    @TableField("k_nickname")
    private String nickname;

    @ApiModelProperty("用户密码")
    @TableField("k_password")
    private String password;

    @TableField("avatar")
    private String avatar;

    @TableField("rating")
    private Integer rating;

    private String color;

}
