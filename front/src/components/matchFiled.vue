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
                <div class="user-select">
                    <select class="form-select" v-model="selected_id" aria-label="Default select example">
                        <option selected="selected"  value="-1">亲自上阵</option>
                        <option v-for='bot in myBots' :key="bot.bid" :value="bot.bid">{{ bot.btitle }}</option>
                    </select>
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
                <div class="user-select">
                    <select v-model="selected_mode"  class="form-select" aria-label="Default select example">
                        <option selected value="-1">选择AI</option>
                        <option  value="1">智能匹配</option>
                        <option  value="2">随机匹配</option>
                    </select>
                </div>  
            </div>
            <div class="col-12" style="text-align: center;padding-top:10vh;">
                <v-btn @click="match" variant="flat" color="error" size="large" style="padding-right:30px">
                    <v-progress-circular indeterminate v-if="ismatching"></v-progress-circular>
                    <span style="padding-left:15px">{{ matchStatus }}</span>
                </v-btn>
            </div>
        </div>

    </div>
</template>

<script>
import { useStore } from 'vuex';
import { ref } from "vue";
import request from '@/util/request'
import { ElMessage } from 'element-plus';
export default {

    setup() {
        let selected_id = ref('')
        let selected_mode = ref('')
        const store = useStore();
        let ismatching = ref(false)
        let matchStatus = ref('开始匹配')
        let myBots = ref([]);
        let match = () => {
            ismatching.value = !ismatching.value
            console.log(selected_id.value);
            if(selected_id.value==null || selected_mode.value==null){
                ElMessage.error("请选择匹配模式!");
                return;
            } 
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
                    event: 'start-matching',
                    bId:selected_id.value,
                    type:selected_mode.value
                }))
            }
        }
        let user = JSON.parse(localStorage.getItem("user"));
        const getBots = () => {
            request({
                url: `/bot/getList/${user.userId}`,
                method: "get",
            }).then((res) => {
                myBots.value = res.data;
            });
            };
            getBots()
        return {
            ismatching, match, matchStatus,getBots,myBots,selected_id,selected_mode
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
.user-select{
    margin: 0 auto;
    text-align: center;
    justify-content: center;
    align-items: center;
    width: 10vw;
    margin-top: 15px;
    margin-left: 5vw;
}
</style>
