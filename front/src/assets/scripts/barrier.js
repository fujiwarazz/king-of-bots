import { GameObject } from './GameObject'

export class Barrier extends GameObject {
    constructor(r, c, gamemap) {
        super();
        this.c = c;
        this.r = r;
        this.gamemap = gamemap;
        this.color = '#B37226'
    }
    update() {
        this.render();
    }
    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle = this.color
        ctx.fillRect(this.c*L,this.r*L,L,L);
    }
}