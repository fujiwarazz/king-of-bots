package com.kob.controller;

import com.kob.model.dto.RecordsPageDto;
import com.kob.service.IRecordsService;
import com.kob.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author peelsannaw
 * @create 06/01/2023 11:16
 */
@RestController
@RequestMapping("/records")
public class RecordController {
    @Resource
    private IRecordsService recordsService;


    @PostMapping("/list")
    public ResponseResult<?>getRecords(@RequestBody RecordsPageDto recordsPageDto){
        return recordsService.getRecordsPage(recordsPageDto);
    }
}
