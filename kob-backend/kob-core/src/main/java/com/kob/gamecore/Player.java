package com.kob.gamecore;

import com.kob.model.entity.Bot;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author peelsannaw
 * @create 09/01/2023 10:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long id;
    private Integer sx;
    private Integer sy;
    private List<Integer> step;
    private Bot bot;
    public boolean isSnakeTailIncreasing(int step) {
        if (step < 10) {
            return true;
        }
        return step % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell>cells = new ArrayList<>();
        int []dx = {-1,0,1,0};
        int []dy = {0,1,0,-1};
        int x = sx,y = sy;
        int step = 0;
        cells.add(new Cell(x,y));
        for (int integer : this.step) {
            x += dx[integer];
            y += dy[integer];
            cells.add(new Cell(x,y));
            ++step;
            if(!isSnakeTailIncreasing(step)){
                cells.remove(0);
            }
        }
        return cells;
    }
}
