<template>
    <div class="matchFiled">

        <div class="row">
            <div class="col-5">
                <div class="user-avatar">
                    <img :src="$store.state.user.avatar" alt="" />
                </div>
                <div class="user-nickname" style="color:red">
                    {{ $store.state.user.nickname }}
                </div>
            </div>
            <div class="col-2 align-items-center" style="text-align: center; padding-top: 16vh; font-size: 50px; color: #B00020;">
                <span>V</span>
                <span style="color:#409EFF">S</span>

            </div>
            <div class="col-5">
                <div class="user-avatar">
                    <img :src="$store.state.combat.opponent_avatar" style="border:1px" alt="" />
                </div>
                <div class="user-nickname" style="color:grey">
                    {{ $store.state.combat.opponent_nickname }}
                </div>
            </div>
            <div class="col-12" style="text-align: center;padding-top:15vh;">
                <v-btn @click="match" variant="flat" color="error" size="large" style="padding-right:30px">
                    <v-progress-circular indeterminate v-if="ismatching"></v-progress-circular>
                    <span style="padding-left:15px">{{ matchStatus }}</span>

                </v-btn>
            </div>
        </div>

    </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex';
// import { watch } from "vue";

export default {

    setup() {
        const store = useStore();
        let ismatching = ref(false)
        let matchStatus = ref('开始匹配')
        let match = () => {
            ismatching.value = !ismatching.value
            if (!ismatching.value) {
                matchStatus.value = '开始匹配'
                store.commit("updateOpponent", {
                    opponent_nickname: '你的对手',
                    opponent_avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',

                })
                store.state.combat.socket.send(JSON.stringify({
                    event: 'end-matching'
                }))

            } else {
                matchStatus.value = '取消匹配'
                store.commit("updateOpponent", {
                    opponent_nickname: 'finding....',
                    opponent_avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',

                })
                store.state.combat.socket.send(JSON.stringify({
                    event: 'start-matching'
                }))
            }
        }
        // watch(
        //     store=>store.state.combat.getStatus,
        //     (newVal,oldVal) => {
        //         // to do
        //         console.log(newVal,oldVal)
        //         if(newVal=="playing"){
        //             matchStatus.value='拒绝匹配'
        //             ismatching.value=true
        //         }
        //     }
        // );

        return {
            ismatching, match, matchStatus
        }
    },
};
</script>

<style scoped>
div.matchFiled {
    width: 50vw;
    height: 70vh;
    margin: 0 auto;
    margin-top: 40px;
    background-color: rgb(255, 255, 255, 0.5);
}

.user-avatar {
    text-align: center;
    padding-top: 10vh;
}

.user-avatar>img {
    border-radius: 50%;
    height: 20vh;
    border: 3px white solid;
}

.user-nickname {
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    padding-top: 2vh;
}
</style>
