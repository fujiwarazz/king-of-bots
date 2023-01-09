package com.kob.gamecore;

import com.alibaba.fastjson.JSONObject;
import com.kob.model.entity.Records;
import com.kob.websocket.Consumer;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.record.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author peelsannaw
 * @create 08/01/2023 17:20
 */
@Slf4j
public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer innerWallsCount;
    private final int[][] gameMap;
    public static final int[] DX = {1, 0, -1, 0};
    public static final int[] DY = {0, 1, 0, -1};
    private final Player playerA;
    private final Player playerB;
    private boolean isOver = false;
    private String loser = null;
    //保证进程之间修改的可感知
    volatile private Integer nextStepA = null;
    volatile private Integer nextStepB = null;

    private final ReentrantLock reentrantLock = new ReentrantLock();

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Long id1, Long id2) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.gameMap = new int[rows][cols];
        this.playerA = new Player(id1, rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(id2, 1, cols - 2, new ArrayList<>());
    }

    public void setNextStepA(Integer nextStepA) {

        //锁加volatile 保证原子性和可见性和顺序
        reentrantLock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            reentrantLock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        reentrantLock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            reentrantLock.unlock();
        }
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public int[][] getGameMap() {
        return this.gameMap;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                gameMap[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r++) {
            gameMap[r][0] = gameMap[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            gameMap[0][c] = gameMap[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (gameMap[r][c] == 1 || gameMap[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) {
                    continue;
                }

                gameMap[r][c] = gameMap[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return isAccessible(this.rows - 2, 1, 1, this.cols - 2);
    }


    private boolean isAccessible(int sx, int sy, int ex, int ey) {
        if (sx == ex && sy == ey) {
            return true;
        }
        gameMap[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int x = sx + DX[i];
            int y = sy + DY[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && gameMap[x][y] == 0) {
                if (isAccessible(x, y, ex, ey)) {
                    gameMap[sx][sy] = 0;
                    return true;
                }
            }
        }

        gameMap[sx][sy] = 0;
        return false;
    }

    public void createGameMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }

    //等待下一步操作
    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //5s内判断有没有输入
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                reentrantLock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {

                        playerA.getStep().add(nextStepA);
                        playerB.getStep().add(nextStepB);

                        return true;
                    }
                } finally {
                    reentrantLock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendAllMsg(String msg){
        ConcurrentHashMap<Long, Consumer> usersLink = Consumer.USERS_LINK;

        System.out.println(msg);
        if (usersLink.get(playerA.getId())!=null){
            usersLink.get(playerA.getId()).sendMsg(msg);

        }
        if(usersLink.get(playerB.getId())!=null){
            usersLink.get(playerB.getId()).sendMsg(msg);

        }
    }
    /**
     * 返回结果给AB
     */
    private void sendResult() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event","result");
        jsonObject.put("loser",loser);
        sendAllMsg(jsonObject.toJSONString());
        saveToDb();

    }

    /**
     * 判断下一步是否合法
     */
    private void judge(){
        List<Cell> cellsA= playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean isValidA = checkCellValid(cellsA,cellsB);
        boolean isValidB = checkCellValid(cellsB,cellsA);

        if(!isValidA || !isValidB){
            this.isOver = true;

            if(!isValidA && !isValidB){
                this.loser = "all";
            }else if(!isValidA){
                this.loser = "A";
            }else{
                this.loser = "B";
            }
        }

    }

    private void saveToDb(){
        Records records = new Records();
        records.setAId(playerA.getId());
        records.setBId(playerB.getId());
        records.setASx(playerA.getSx());
        records.setASy(playerA.getSy());
        records.setBSx(playerB.getSx());
        records.setBSy(playerB.getSy());
        records.setLoser(this.loser);
        records.setMap(getMapString(this.gameMap));
        records.setASteps(getStepsString(playerA.getStep()));
        records.setBSteps(getStepsString(playerB.getStep()));
        Consumer.recordsMapper.insert(records);
    }

    private String getMapString(int[][] gameMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[0].length; j++) {
                stringBuilder.append(gameMap[i][j]);
            }
        }
        return stringBuilder.toString();
    }

    private String getStepsString(List<Integer> step) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer : step) {
            stringBuilder.append(integer.toString());
        }
        return stringBuilder.toString();
    }

    private boolean checkCellValid(List<Cell>A,List<Cell>B){
        int n = A.size();
        Cell cell = A.get(n - 1);
        //A是否撞墙
        if(gameMap[cell.x][cell.y]==1){
            return false;
        }
        //A是否自己撞自己
        for(int i = 0;i<n-1;i++){
            if(A.get(i).x==cell.x && A.get(i).y==cell.y){
                return false;
            }
        }
        for(int i = 0;i<B.size();i++){
            if(cell.x == B.get(i).x && cell.y == B.get(i).y){
                return false;
            }
        }
        return true;
    }
    /**
     * 返回移动信息
     */
    private void sendMove(){
        reentrantLock.lock();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event","move");
            jsonObject.put("a_direction",nextStepA);
            jsonObject.put("b_direction",nextStepB);
            sendAllMsg(jsonObject.toJSONString());
            nextStepA = null;
            nextStepB = null;

        }finally {
            reentrantLock.unlock();
        }

    }
    @Override
    public void run() {
        log.info("线程开始run,id:[{}]",Thread.currentThread().getId());
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                judge();
                if(!isOver){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            } else {
                reentrantLock.lock();
                try {
                    isOver = true;
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    reentrantLock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
