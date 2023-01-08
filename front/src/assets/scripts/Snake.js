import { GameObject } from "./GameObject";
import { Cell } from "./Cell";

export class Snake extends GameObject {
    constructor(info, gameMap) {
        super();
        this.id = info.id;
        this.color = info.color;
        this.gameMap = gameMap;
        //身体 cells[0]为头
        this.cells = [new Cell(info.r, info.c)];
        //蛇的速度 5格子/s
        this.speed = 4;
        this.direction = -1; // 0 1 2 3表示上右下左
        this.status = "idle";//idle: 静止 move:移动 die:死亡
        this.next_cell = null;
        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];
        this.step = 0;
        this.eps = 1e-3;
        this.head_direction = 0; //上
        if (this.id == 1) {
            this.head_direction = 2; //下
        }
        //眼睛偏移量
        this.eyes_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1]
        ];
        this.eyes_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1]
        ];
    }
    start() {

    }
    //每一帧一次
    update() {
        if (this.status == "move") {
            this.update_position();
        }
        this.render();

    }
    //实时判断位置,改变状态 
    update_position() {
        const move_distance = this.speed * this.time_delta / 1000;//一帧走的距离
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        //因为这里是一帧刷新一次,所以要判断是否到达下一个点的(因为一次只能走一步)
        if (distance < this.eps) {
            this.cells[0] = this.next_cell; //新的头
            //到达下一帧的位置,停止移动
            this.status = "idle";
            this.next_cell = null;

            if (!this.is_tail_increasing()) {
                this.cells.pop(); //js pop出的是尾部 unshift出的是头
            }

        } else {
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
            //蛇的长度变化: 头向前移动,尾巴不一定要向前移动
            //蛇尾不需要变长,那么蛇尾就需要走到下个地点
            if (!this.is_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }

    }


    //检测当前回合蛇的长度是否增加
    is_tail_increasing() {
        if (this.step < 10) return true;
        else if (this.step % 3 === 1) return true;
        return false;
    }
    //将蛇的状态转化为下一步
    next_step() {
        const d = this.direction;
        const head = this.cells[0];
        this.next_cell = new Cell(head.r + this.dr[d], head.c + this.dc[d]);
        this.direction = -1;//清空操作
        this.status = "move";
        this.step++;
        this.head_direction = d;

        const count = this.cells.length;
        for (let i = count; i > 0; i--) {
            //深拷贝
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
        //下一步操作非法
        if (!this.gameMap.isValid(this.next_cell)) {
            this.status = "die";

        }


    }
    render() {
        //获取地图信息
        const L = this.gameMap.L;
        const ctx = this.gameMap.ctx;

        ctx.fillStyle = this.color;
        if (this.status === "die") {
           
            ctx.fillStyle = 'white'
        }
        for (let cell of this.cells) {

            ctx.beginPath();
            //画身体
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        ctx.fillStyle = 'black';
        for(let i = 0;i<2;i++){
            const ex = (this.cells[0].x + this.eyes_dx[this.head_direction][i] *0.15)*L;
            const ey = (this.cells[0].y + this.eyes_dy[this.head_direction][i] *0.15)*L;
            ctx.beginPath();
            ctx.arc(ex,ey,L*0.06,0,Math.PI*2)
            ctx.fill();
        }

    }
    set_direction(d) {
        this.direction = d;
    }
}