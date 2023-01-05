/* eslint-disable no-unused-vars */

 
const GAME_OBJECT =[];

export class GameObject {
    constructor(){
        GAME_OBJECT.push(this);
        this.time_delta = 0;
        this.has_called_start = false;
    }
    //开始执行
    start(){

    }
    //每一帧执行一次
    update(){

    }
    //删除之前的hook
    on_destroy(){

    }
    destroy() {
        this.on_destroy()
        for(let i in GAME_OBJECT){
            let obj = GAME_OBJECT[i];
            if(obj == this){
                GAME_OBJECT.splice(i);
                break;
            }
        }
    }



}
let last_timestamp = 0;//上一次
const step = now_timestamp =>{
    for(let obj of GAME_OBJECT){
        if(!obj.has_called_start){
            obj.start();
            obj.has_called_start = true;
        }else{
            obj.time_delta = now_timestamp - last_timestamp;
            obj.update();
            
        }
    }
    last_timestamp = now_timestamp;

    requestAnimationFrame(step);
}

requestAnimationFrame(step)
