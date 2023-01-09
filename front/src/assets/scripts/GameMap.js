/* eslint-disable no-unused-vars */

import { GameObject } from "./GameObject";
import {  Barrier } from './barrier'
import { Snake } from "./Snake";
import store from "@/store";
    class node{
    constructor(x,y){
        this.x = x;
        this.y =y;
    }
}
export class GameMap extends GameObject {
    constructor(ctx, parent,number,store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;

        this.rows = 13;
        this.cols = 14;
        
        this.inner_walls_count = 20;
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];

        
    }

    start() {
        this.create_walls();
        
        this.add_listener_events();
    }

    //检测状态是否合法
    isValid(cell){
        //是否撞墙
        for(let wall of this.walls){
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
        const g = store.state.combat.gameMap;

        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if (g[r][c]) {
                    this.walls.push(new Barrier(r, c, this));
                }
            }
        }
    }


  
    //canvas监听输入    
    add_listener_events(){
        this.ctx.canvas.focus();
        this.ctx.canvas.addEventListener("keydown",e=>{
            let d = -1;
            if(e.key=='w') d=0;
            else if(e.key =='d') d=1;
            else if(e.key=='s') d=2;
            else if(e.key=='a') d=3;
            if(d>=0 && d<=3){
                store.state.combat.socket.send(JSON.stringify({
                    event:'move',
                    direction:d
                }))
            }
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
    check_valid(cell) {  // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.is_tail_increasing()) {  // 当蛇尾会前进的时候，蛇尾不要判断
                k -- ;
            }
            for (let i = 0; i < k; i ++ ) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
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

