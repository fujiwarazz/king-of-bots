package com.kob.enumnation;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author peelsannaw
 * @create 07/01/2023 23:53
 */
public enum LangEnum {

    CPP("cpp"),
    JAVA("java"),
    PYTHON("python"),
    GO("go"),
    JAVASCRIPT("javascript");


    @EnumValue
    String value;

    LangEnum(String value) {
        this.value = value;
    }
}
