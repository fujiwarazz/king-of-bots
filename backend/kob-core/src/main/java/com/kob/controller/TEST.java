package com.kob.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;
import java.util.Map;

/**
 * @Author peelsannaw
 * @create 04/01/2023 21:14
 */
@RestController
public class TEST {

    @GetMapping("/test")
    public Map<String,String>getInfo(){
        Map<String,String>m = new Hashtable<>();
        m.put("bot01","100");
        m.put("bot02","200");
        return m;
    }

}
