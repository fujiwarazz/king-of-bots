package com.kob.service.impl;

import com.kob.model.entity.Records;
import com.kob.mapper.RecordsMapper;
import com.kob.service.IRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 对局记录 服务实现类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-09
 */
@Service
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Records> implements IRecordsService {

}
