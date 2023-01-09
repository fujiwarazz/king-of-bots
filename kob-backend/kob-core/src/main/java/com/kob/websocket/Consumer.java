package com.kob.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.constant.GameConstant;
import com.kob.gamecore.Game;
import com.kob.mapper.RecordsMapper;
import com.kob.mapper.UserMapper;
import com.kob.model.dto.MatchingDto;
import com.kob.model.entity.User;
import com.kob.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.*;


/**
 * @Author peelsannaw
 * @create 08/01/2023 10:02
 */
@Component
@Slf4j
@ServerEndpoint("/ws/{token}")
public class Consumer {


    public static UserMapper userMapper;

    private static StringRedisTemplate stringRedisTemplate;

    public static RecordsMapper recordsMapper;

    private Session session = null;

    private User user;

    private Game game = null;


    /**
     * 用户id与服务端
     */
    public final static ConcurrentHashMap<Long, Consumer> USERS_LINK = new ConcurrentHashMap<Long, Consumer>();

    public final static CopyOnWriteArrayList<User>MATCHING_POOL = new CopyOnWriteArrayList<>();


    //websocket注入需要用setter
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        Consumer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordsMapper(RecordsMapper recordsMapper) {
        Consumer.recordsMapper = recordsMapper;
    }


    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        Consumer.stringRedisTemplate = stringRedisTemplate;
    }



    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        try {
            token = token.replaceAll("\"","");
            log.error("[{}]用户建立连接,session为:[{}],线程为:[{}]", token, session.getId(),Thread.currentThread().getId());
            this.session = session;

            System.out.println("kob_token:login:last-activity:" + token);
            String s = stringRedisTemplate.opsForValue().get("kob_token:login:last-activity:" + token);

            if (s != null) {
                String useId = token.substring(50);
                byte[] decode = Base64Utils.decode(useId);
                assert decode != null;
                String userid = new String(decode);
                this.user = userMapper.selectById(userid);
                System.out.println(JSON.toJSONString(this.user));
                USERS_LINK.put(this.user.getId(),this);
            }else{
                this.session.close();
            }

        } catch (Exception e) {
            //token失效断开连接
            e.printStackTrace();
            this.session.close();
        }


    }

    @OnClose
    public void onClose() {
        // 关闭链接
        log.info("用户关闭连接,session为:[{}]", session.getId());
        if (this.user != null) {
            USERS_LINK.remove(this.user.getId());
            MATCHING_POOL.remove(this.user);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws InterruptedException {
        // 从Client接收消息
        MatchingDto matchingDto = JSON.parseObject(message, MatchingDto.class);
        System.out.println(matchingDto.getEvent());
        if(GameConstant.START_MATCHING.equals(matchingDto.getEvent())){
            handleMatching();
        }else if(GameConstant.END_MATCHING.equals(matchingDto.getEvent())){
            stopMatching();
        }else if(GameConstant.MOVE.equals(matchingDto.getEvent())){
            move(matchingDto.getDirection());
        }
    }


    private void move(Integer direction){
        if(game.getPlayerA().getId().equals(this.user.getId())){
            log.info("A:当前线程为:[{}]",Thread.currentThread().getId());
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(this.user.getId())){
            log.info("B:当前线程为:[{}]",Thread.currentThread().getId());
            game.setNextStepB(direction);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    private void handleMatching() throws InterruptedException {
        System.out.println("开始匹配");
        MATCHING_POOL.add(this.user);
        while(MATCHING_POOL.size()>=2){
            Iterator<User> iterator = MATCHING_POOL.iterator();
            User a = iterator.next();
            User b = iterator.next();
            MATCHING_POOL.remove(a);
            MATCHING_POOL.remove(b);

            //生成地图
            Game game = new Game(13,14,30,a.getId(),b.getId());
            game.createGameMap();

            USERS_LINK.get(a.getId()).game = game;
            USERS_LINK.get(b.getId()).game = game;


            game.start();


            JSONObject responseGame = new JSONObject();
            responseGame.put("a_id",game.getPlayerA().getId());
            responseGame.put("b_id",game.getPlayerB().getId());
            responseGame.put("a_sx",game.getPlayerA().getSx());
            responseGame.put("a_sy",game.getPlayerA().getSy());
            responseGame.put("b_sx",game.getPlayerB().getSx());
            responseGame.put("b_sy",game.getPlayerB().getSy());
            responseGame.put("map",game.getGameMap());
            //匹配成功
            JSONObject respA = new JSONObject();
            respA.put("event",MatchingDto.event.START.getName());
            respA.put("opponent_nickname",b.getNickname());
            respA.put("opponent_avatar",b.getAvatar());
            respA.put("opponent_id",b.getId());
            respA.put("gameMap",responseGame);


            JSONObject respB = new JSONObject();
            respB.put("event",MatchingDto.event.START.getName());
            respB.put("opponent_nickname",a.getNickname());
            respB.put("opponent_avatar",a.getAvatar());
            respB.put("opponent_id",a.getId());
            respB.put("gameMap",responseGame);

            USERS_LINK.get(a.getId()).sendMsg(respA.toJSONString());
            USERS_LINK.get(b.getId()).sendMsg(respB.toJSONString());

            System.out.println(respA.toJSONString());
            System.out.println(respB.toJSONString());
        }
    }
    private void stopMatching(){
        System.out.println("停止匹配");
        if(!MATCHING_POOL.contains(this.user)){
            //发送信息给前端要求取消匹配

        }else{
            MATCHING_POOL.remove(this.user);

        }
    }
    public void sendMsg(String message) {

        synchronized (this.session.getId()){
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
