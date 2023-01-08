// import request from '../util/request'
export default{
    state: {
        socket:null,
        opponent_id:'',
        opponent_nickname:'',
        opponent_avatar:'',
        status:'matching' ,//matching,playing
        gameMap:null,
    },
    getters: {
        getStatus:(state)=>state.status
    },
    mutations: {
        updateSocket(state,socket){
            state.socket =  socket; 
        },
        updateOpponent(state,opponent){
            state.opponent_id = opponent.opponent_id
            state.opponent_nickname = opponent.opponent_nickname
            state.opponent_avatar = opponent.opponent_avatar
        },
        updateStatus(state,status){
            state.status = status
        },
        updateGameMap(state,gameMap){
            state.gameMap = gameMap
        }
    },
    actions: {
       
    },
    modules: {
    }
}