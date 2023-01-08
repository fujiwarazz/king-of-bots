package com.kob.gamecore;

import io.swagger.models.auth.In;

import java.util.Arrays;
import java.util.Random;

/**
 * @Author peelsannaw
 * @create 08/01/2023 17:20
 */
public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer innerWallsCount;
    final private int[][] gameMap;
    final static int[] DX = {1, 0, -1, 0};
    final static int[] DY = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer innerWallsCount) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.gameMap = new int[rows][cols];
    }

    public int[][] getGameMap() {
        return this.gameMap;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                gameMap[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            gameMap[r][0] = gameMap[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            gameMap[0][c] = gameMap[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
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


    private boolean isAccessible(int sx,int sy,int ex,int ey) {
        if(sx==ex &&  sy == ey) {
            return true;
        }
        gameMap[sx][sy] = 1;
        for(int i = 0;i<4;i++){
            int x = sx + DX[i];
            int y = sy + DY[i];
            if(x>=0 && x<this.rows && y>=0 && y<this.cols && gameMap[x][y]==0) {
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

    public static void main(String[] args) {
        Game game = new Game(13, 14, 20);
        game.createGameMap();
        System.out.println(Arrays.deepToString(game.getGameMap()));
    }
}
