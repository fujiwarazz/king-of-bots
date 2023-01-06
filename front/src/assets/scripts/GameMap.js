/* eslint-disable no-unused-vars */

import { GameObject } from "./GameObject";
import {  Barrier } from './barrier'
import { Snake } from "./Snake";
    class node{
    constructor(x,y){
        this.x = x;
        this.y =y;
    }
}
export class GameMap extends GameObject {
    constructor(ctx, parent,number) {
        super();
        //绝对距离
        this.L = 0;
        this.ctx = ctx;
        this.parent = parent;
        this.cols = 14;
        this.rows = 13;

        this.barriers = [];
        this.inner_walls_count = number?number:50;

        this.snakes = [
            new Snake({id:0,color:"#4876EC",r:this.rows-2,c:1},this),
            new Snake({id:1,color:"#F94848",r:1,c:this.cols-2},this),
        ]
        
    }

    start() {
        for(let i = 0;i<1000;i++){
            if(this.create_walls()) break;
        }
        this.add_listener_events();
    }
    //检测状态是否合法
    isValid(cell){
        //是否撞墙
        for(let wall of this.barriers){
          if(wall.r == cell.r && wall.c == cell.c){
            return false;
          }
        }
        //检查每条蛇和这个cell是否有碰撞
        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.is_tail_increasing()){
                k--;
            }
            for(let i = 0;i<k;i++){
                if(snake.cells[i].r===cell.r &&snake.cells[i].c ===cell.c){
                    return false;
                }
            }
        }
        return true;
    
    }
    //地图是否联通 
    //TODO:加入初始化地图过程
    isAccess(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i ++ ) {
            let x = sx + dx[i], y = sy + dy[i];
            if (!g[x][y] && this.isAccess(g, x, y, tx, ty))
                return true;
        }
        return false;
    }
    //判断两条蛇是否准备好
    isReady(){
        //两条蛇的状态都需要为idle,并且有下一步输入
        for(let snake of this.snakes){
            if(snake.status!=="idle"){
                return false;
            }
            if(snake.direction===-1){
                return false;
            }
        }
        return true;
    }
    create_walls() {
        const g = [];
        for (let r = 0; r < this.rows; r ++ ) {
            g[r] = [];
            for (let c = 0; c < this.cols; c ++ ) {
                g[r][c] = false;
            }
        }

        // 给四周加上障碍物
        for (let r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = true;
        }

        for (let c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // 创建随机障碍物
        for (let i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (let j = 0; j < 1000; j ++ ) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;
                //中心对称
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
                break;
            }
        }

        const copy_g = JSON.parse(JSON.stringify(g));
        if (!this.isAccess(copy_g, this.rows - 2, 1, 1, this.cols - 2))
            return false;

        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if (g[r][c]) {
                    this.barriers.push(new Barrier(r, c, this));
                }
            }
        }

        return true;
    }
  
    //canvas监听输入
    add_listener_events(){
        this.ctx.canvas.focus();
        const [snake0,snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown",e=>{
            if(e.key=='w') snake0.set_direction(0);
            else if(e.key =='d') snake0.set_direction(1);
            else if(e.key=='s') snake0.set_direction(2);
            else if(e.key=='a') snake0.set_direction(3);
            else if(e.key=='ArrowUp') snake1.set_direction(0);
            else if(e.key =='ArrowRight') snake1.set_direction(1);
            else if(e.key=='ArrowDown') snake1.set_direction(2);
            else if(e.key=='ArrowLeft') snake1.set_direction(3);

        });
    }

    //让两条蛇进入下一步
    next_step(){
        for(let s of this.snakes){
            s.next_step();
        }
    }
    update() {
        this.update_size();
        if(this.isReady()){
            this.next_step();
        }
      
        this.render();
    }
    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));

        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    render() {
        const color_even = '#A2D149';
        const color_odd = '#AAD751';
        for(let r = 0;r<this.rows;r++){
            for(let c = 0;c<this.cols;c++){
                if((r+c)%2==1){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L);
            }
        }
        

    }
}1

