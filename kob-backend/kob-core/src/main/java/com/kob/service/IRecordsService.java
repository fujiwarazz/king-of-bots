package com.kob.service;

import com.kob.model.dto.RecordsPageDto;
import com.kob.model.entity.Records;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.util.ResponseResult;
import org.springframework.data.domain.PageRequest;

/**
 * <p>
 * 对局记录 服务类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-09
 */
public interface IRecordsService extends IService<Records> {

    ResponseResult<?> getRecordsPage(RecordsPageDto pageDto);
}
