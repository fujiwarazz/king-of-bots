package com.kob.util;

import com.kob.model.Player;
import com.kob.model.PlayerMatchDto;
import com.kob.model.PlayerMatchSuccessDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author peelsannaw
 * @create 09/01/2023 22:55
 */
@SuppressWarnings("all")
@Component
@Slf4j
public class MatchingPool extends Thread {

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    private volatile static CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    public static final String REMOTE_API = "http://127.0.0.1:9999/combat/start";

    public void addPlayer(Player player) {
        lock.lock();
        try {
            players.add(player);
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Long playerId) {
        lock.lock();
        try {
            CopyOnWriteArrayList<Player> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (Player p : players) {
                if (!p.getUserId().equals(playerId)) {
                    copyOnWriteArrayList.add(p);
                }
            }
            players = copyOnWriteArrayList;
        } finally {
            lock.unlock();
        }
    }

    private void increasingPlayersWaitingTime() {
        //设置匹配时间++
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private void matchPlayers() {
        System.out.println("匹配池内玩家:"+players.toString());
        boolean[] st = new boolean[players.size()];
        //从前向后,判断这名玩家是否能够有匹配对象
        for (int i = 0; i < players.size(); i++) {
            if (st[i]) {
                continue;
            }
            for (int j = i + 1; j < players.size(); j++) {
                if (st[j]) {
                    continue;
                }
                Player a = players.get(i);
                Player b = players.get(j);
                if (isPlayerMatch(a, b)) {
                    st[i] = st[j] = true;
                    log.info("匹配成功,对象为:[{}][{}]",players.get(i).getUserId(),players.get(j).getUserId());
                    sendResult(a, b);
                    break;
                }
            }
        }
        CopyOnWriteArrayList<Player> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        for (int j = 0; j < players.size(); j++) {
            if (!st[j]) {
                copyOnWriteArrayList.add(players.get(j));
            }
        }
        players = copyOnWriteArrayList;
    }

    /**
     * 判断两个玩家是否匹配
     *
     * @return
     */

    private boolean isPlayerMatch(Player a, Player b) {
        int ratingGap = Math.abs(a.getRating() - b.getRating());
        //双方的匹配时间最小值,满足要求的最小值
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingGap <= waitingTime * 10;
    }

    /**
     * 返回匹配结果
     *
     * @param a 玩家a
     * @param b 玩家b
     */
    private void sendResult(Player a, Player b) {
        MultiValueMap<String,String>multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("a_id",a.getUserId().toString());
        multiValueMap.add("b_id",b.getUserId().toString());
        multiValueMap.add("a_bid",a.getBid().toString());
        multiValueMap.add("b_bid",b.getBid().toString());
        System.out.println(multiValueMap);
        restTemplate.postForObject(REMOTE_API,multiValueMap, String.class);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increasingPlayersWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
