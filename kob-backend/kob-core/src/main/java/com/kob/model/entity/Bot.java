package com.kob.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2023-01-07
 */
@Getter
@Setter
@TableName("k_bot")
@ApiModel(value = "Bot对象", description = "")
public class Bot implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "b_id",type = IdType.ASSIGN_ID)
    private Long bId;

    @TableField(value = "k_id")
    private Long kId;

    @TableField(value = "b_title")
    private String bTitle;

    @TableField(value = "b_desc")
    private String bDesc;

    @TableField(value = "b_code")
    private String bCode;

    @TableField(value = "b_rating")
    private Integer bRating;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

    @ApiModelProperty("是否公开")
    @TableField(value = "b_is_open")
    private Integer bIsOpen;

    @ApiModelProperty("1:表示正常使用 2:表示停用")
    @TableField(value = "b_status")
    private Integer bStatus;

}
