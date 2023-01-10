package com.kob.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.MatchClient;
import com.kob.constant.GameConstant;
import com.kob.gamecore.Game;
import com.kob.mapper.BotMapper;
import com.kob.mapper.RecordsMapper;
import com.kob.mapper.UserMapper;
import com.kob.model.ResponseResult;
import com.kob.model.dto.MatchingDto;
import com.kob.model.dto.PlayerMatchDto;
import com.kob.model.entity.Bot;
import com.kob.model.entity.User;
import com.kob.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    public static BotMapper botMapper;

    private Session session = null;

    private User user;

    public Game game = null;

    public static RestTemplate restTemplate;

    public static MatchClient matchClient;

    private final static String REMOTE_ADD_PLAYER_URL="http://127.0.0.1:9998/match/player/add";
    private final static String REMOTE_REMOVE_PLAYER_URL="http://127.0.0.1:9998/match/player/remove";
    /**
     * 用户id与服务端
     */
    public final static ConcurrentHashMap<Long, Consumer> USERS_LINK = new ConcurrentHashMap<Long, Consumer>();


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        Consumer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordsMapper(RecordsMapper recordsMapper) {
        Consumer.recordsMapper = recordsMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    @Autowired
    public void setMatchClient(MatchClient matchClient) {
        Consumer.matchClient = matchClient;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        Consumer.stringRedisTemplate = stringRedisTemplate;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        Consumer.botMapper = botMapper;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        try {
            token = token.replaceAll("\"", "");
            log.error("[{}]用户建立连接,session为:[{}],线程为:[{}]", token, session.getId(), Thread.currentThread().getId());
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
                USERS_LINK.put(this.user.getId(), this);
            } else {
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
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws InterruptedException {
        // 从Client接收消息
        MatchingDto matchingDto = JSON.parseObject(message, MatchingDto.class);
        System.out.println(matchingDto.getEvent());
        if (GameConstant.START_MATCHING.equals(matchingDto.getEvent())) {
            handleMatching(matchingDto.getBId(),matchingDto.getType());
        } else if (GameConstant.END_MATCHING.equals(matchingDto.getEvent())) {
            stopMatching();
        } else if (GameConstant.MOVE.equals(matchingDto.getEvent())) {
            move(matchingDto.getDirection());
        }
    }


    private void move(Integer direction) {
        if (game.getPlayerA().getId().equals(this.user.getId())) {
            log.info("A:当前线程为:[{}]", Thread.currentThread().getId());
            //屏蔽bot输入
            System.out.println(game.getPlayerA().getBot().getBId());

            if(game.getPlayerA().getBot().getBId()==-1L){
                game.setNextStepA(direction);
            }
        } else if (game.getPlayerB().getId().equals(this.user.getId())) {
            log.info("B:当前线程为:[{}]", Thread.currentThread().getId());
            System.out.println(game.getPlayerA().getBot().getBId());
            if(game.getPlayerB().getBot().getBId()==-1L){
                game.setNextStepB(direction);
            }
        }
    }

    public static void startGame(Long id1, Long id2,Long user1Bid,Long user2Bid) {
        User a = userMapper.selectById(id1);
        User b = userMapper.selectById(id2);
        Bot botA = new Bot();
        Bot botB = new Bot();
        botA.setBCode("");
        botB.setBCode("");
        if(user1Bid!=-1L){
            botA = botMapper.selectById(user1Bid);
        }else{
            botA.setBId(-1L);
        }
        if(user2Bid!=-1L){
            botB = botMapper.selectById(user2Bid);
        }else{
            botB.setBId(-1L);
        }
        //生成地图
        Game game = new Game(13, 14, 30, a.getId(), b.getId(),botA,botB);
        game.createGameMap();

        if(USERS_LINK.get(a.getId())!=null){
            USERS_LINK.get(a.getId()).game = game;
        }
        if(USERS_LINK.get(b.getId())!=null){
            USERS_LINK.get(b.getId()).game = game;
        }

        log.info("game开始");
        game.start();


        JSONObject responseGame = new JSONObject();
        responseGame.put("a_id", game.getPlayerA().getId());
        responseGame.put("b_id", game.getPlayerB().getId());
        responseGame.put("a_sx", game.getPlayerA().getSx());
        responseGame.put("a_sy", game.getPlayerA().getSy());
        responseGame.put("b_sx", game.getPlayerB().getSx());
        responseGame.put("b_sy", game.getPlayerB().getSy());
        responseGame.put("map", game.getGameMap());
        //匹配成功
        JSONObject respA = new JSONObject();
        respA.put("event", MatchingDto.event.START.getName());
        respA.put("opponent_nickname", b.getNickname());
        respA.put("opponent_avatar", b.getAvatar());
        respA.put("opponent_id", b.getId());
        respA.put("gameMap", responseGame);


        JSONObject respB = new JSONObject();
        respB.put("event", MatchingDto.event.START.getName());
        respB.put("opponent_nickname", a.getNickname());
        respB.put("opponent_avatar", a.getAvatar());
        respB.put("opponent_id", a.getId());
        respB.put("gameMap", responseGame);

        if( USERS_LINK.get(a.getId())!=null){
            USERS_LINK.get(a.getId()).sendMsg(respA.toJSONString());
        }
        if( USERS_LINK.get(b.getId())!=null){
            USERS_LINK.get(b.getId()).sendMsg(respB.toJSONString());
        }

        System.out.println(respA.toJSONString());
        System.out.println(respB.toJSONString());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    /**
     *
     * @param botId 选择的botId ,如果为1则是自己
     * @param matchType 选择的模式 -1：AI 1：只能匹配 2：随机匹配
     */
    private void handleMatching(Long botId,Integer matchType) {
        System.out.println("开始匹配");
        PlayerMatchDto playerMatchDto = new PlayerMatchDto();
        playerMatchDto.setUserId(this.user.getId());
        playerMatchDto.setRating(this.user.getRating());
        playerMatchDto.setBId(botId);
        playerMatchDto.setMatchType(matchType);
        restTemplate.postForObject(REMOTE_ADD_PLAYER_URL,playerMatchDto, ResponseResult.class);
    }

    private void stopMatching() {
        System.out.println("停止匹配");
        PlayerMatchDto playerMatchDto = new PlayerMatchDto();
        playerMatchDto.setUserId(this.user.getId());
        restTemplate.postForObject(REMOTE_REMOVE_PLAYER_URL,playerMatchDto, ResponseResult.class);
    }

    public void sendMsg(String message) {

        synchronized (this.session.getId()) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
