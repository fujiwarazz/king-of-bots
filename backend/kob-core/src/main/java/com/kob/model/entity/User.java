package com.kob.model.entity;

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
      private Long kId;

    @ApiModelProperty("用户昵称")
    private String kNickname;

    @ApiModelProperty("用户密码")
    private String kPassword;


}
