package com.kob.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 对局记录
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-09
 */
@Getter
@Setter
@TableName("k_records")
@ApiModel(value = "Records对象", description = "对局记录")
public class Records implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "r_id", type = IdType.ASSIGN_ID)
    private Long rId;

    private Long aId;

    private Long bId;

    private Integer aSx;

    private Integer aSy;

    private Integer bSx;

    private Integer bSy;

    private String aSteps;

    private String bSteps;

    private String loser;

    private String map;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
