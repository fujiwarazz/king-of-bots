<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.combat.loser=='all'">
            Draw
        </div>
        <div class="result-board-text" v-else-if="$store.state.combat.loser=='A' && $store.state.combat.a_id==$store.state.user.userId">
            Wasted
        </div>
        <div class="result-board-text" v-else-if="$store.state.combat.loser=='B' && $store.state.combat.b_id==$store.state.user.userId">
            Wasted
        </div>
        <div class="result-board-text" v-else>
            Win
        </div>
       
        <div class="result-board-button">
            <v-btn @click="again" variant="flat" color="lightblue" size="large" style="padding-right:30px">
                    <v-progress-circular indeterminate v-if="ismatching"></v-progress-circular>
                    <span style="padding-left:15px">Try Again</span>

            </v-btn>
        </div>
    </div>
</template>

<script>
import {useStore} from 'vuex'
export default {
    setup () {

        let store = useStore();
        const again = () =>{
            store.commit('updateCombat')
        }


        return {again,}
    }
}
</script>

<style  scoped>
.result-board{
    height: 30vh;
    width: 30vw;
    background-color: rgba(50,100,200,0.8);
    position: absolute;
    top: 30vh;
    left: 35vw;
}
.result-board-text{
    text-align: center;
    font-size: 50px;
    color: whitesmoke;
    font-family:Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    font-style: italic;
    padding-top: 5vh;
}
.result-board-button{
    text-align: center;
    margin-top: 30px;
}
</style>