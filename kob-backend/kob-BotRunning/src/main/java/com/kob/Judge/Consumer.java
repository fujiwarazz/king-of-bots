package com.kob.Judge;

import com.kob.model.ResponseResult;
import com.kob.model.dto.BotMovingDto;
import com.kob.service.JudgeService;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @Author peelsannaw
 * @create 10/01/2023 14:24
 */
@Component
public class Consumer extends Thread {

    private Judge judge;
    private static RestTemplate restTemplate;
    private final static String REMOTE_RECEIVE_URL = "http://127.0.0.1:9999/combat/receive";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    /**
     * 防止死循环,设置timeout
     *
     * @param timeout 运行时间上线
     * @param judge   传入带判定的代码
     */
    public void startWithTimeOut(Long timeout, Judge judge) {
        this.judge = judge;
        this.start();


        //最多等待timeout秒,如果还没结束就杀死线程,返回错误信息
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.interrupt();
        }
        this.interrupt();
    }

    public String addUid(String uid, String code) {
        int k = code.indexOf(" implements com.kob.service.JudgeService");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        JudgeService judgeService =
                Reflect.compile("com.kob.service.impl.JudgeServiceImpl" + uid, addUid(uid, judge.getBotCode())).create().get();
        System.out.println("move_direction: " + judge.getUserId() + ":" + judgeService.getNextDirection(judge.getInput()));
        BotMovingDto botMovingDto = new BotMovingDto();
        botMovingDto.setUserId(judge.userId);
        botMovingDto.setDirection(judgeService.getNextDirection(judge.getInput()));
        System.out.println(botMovingDto);
        restTemplate.postForObject(REMOTE_RECEIVE_URL, botMovingDto, ResponseResult.class);
    }
}
