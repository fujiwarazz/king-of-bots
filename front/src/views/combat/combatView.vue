<template>
  <div v-if="$store.state.combat.status=='playing'">
    <playgroundFieldVue/>
  </div>
  <div v-if="$store.state.combat.status=='matching'">
    <matchFiled/>
  </div>
</template>

<script>
import matchFiled from '@/components/matchFiled.vue'
import playgroundFieldVue from '@/components/playgroundField.vue';
import {onMounted,onUnmounted} from 'vue'
import {useStore} from 'vuex'
export default {

    components:{
        playgroundFieldVue,matchFiled
    },
    setup () {
        const store = useStore();

        let socketUrl = `ws://127.0.0.1:9999/ws/${store.state.user.token}`;

        let socket =null
        onMounted(()=>{

          store.commit("updateOpponent",{
            opponent_nickname:'你的对手',
            opponent_avatar:'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',

          })
          console.log(store.state.combat.opponent_nickname)
          socket = new WebSocket(socketUrl);
          
          socket.onopen =() =>{
              console.log('open !')
              store.commit('updateSocket',socket)
              console.log(store.state.combat.socket)
          }

          socket.onmessage =msg=>{
            const res = JSON.parse(msg.data)
            if(res.event==="start-matching"){
              console.log('匹配成功')
              store.commit("updateOpponent",{
                opponent_id:res.opponent_id,
                opponent_nickname :res.opponent_nickname,
                opponent_avatar: res.opponent_avatar

              })
              
 
              setTimeout(()=>{
                store.commit("updateStatus","playing")
              },2000)
              console.log(res.gameMap )
              store.commit("updateGameMap", res.gameMap)
            }else if(res.event==="end-matching"){
              store.commit("updateOpponent",{
                opponent_id:'',
                opponent_nickname :'',    
                opponent_avatar: ''

              })
            }
          }
          
          socket.onclose = ()=>{
            store.commit("updateStatus","matching")
            console.log('close');
          }

        })

        onUnmounted(()=>{
          socket.close()
          store.commit("updateStatus","matching")
        })

        return {
        }
    }
}
</script>

<style scoped>

</style>