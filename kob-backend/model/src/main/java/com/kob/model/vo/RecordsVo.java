package com.kob.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author peelsannaw
 * @create 10/01/2023 19:37
 */
@Data
public class RecordsVo {
    private Long rId;

    private Long aId;

    private String aUsername;

    private String aAvatar;

    private Long bId;

    private String bAvatar;

    private String bUserName;

    private Integer aSx;

    private Integer aSy;

    private Integer bSx;

    private Integer bSy;

    private String aSteps;

    private String bSteps;

    private String loser;

    private String map;

    private Date createTime;

    private String loserNickname;

    private String winnerNickname;


}
