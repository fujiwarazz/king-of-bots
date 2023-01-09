// import request from '../util/request'
export default{
    state: {
        opponent_nickname: '你的对手',
        opponent_avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
        socket:null,
        opponent_id:'',
     
        status:'matching' ,//matching,playing
        gameMap:null,
        a_id:null,
        b_id:null,
        a_sx:0,
        a_sy:0,
        b_sx:0,
        b_sy:0 ,
        gameObject:null,
        loser:'none'
    },
    getters: {
        getStatus:(state)=>state.status
    },
    mutations: {
        updateCombat(state){

            state.opponent_id=''
            state.opponent_nickname='你的对手',
            state.opponent_avatar='https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
            state.status='matching' //matching,playing
            state.gameMap=null
            state.a_id=null
            state.b_id=null
            state.a_sx=0
            state.a_sy=0
            state.b_sx=0
            state.b_sy=0 
            state.loser='none'
        },
        updateLoser(state,loser){
            state.loser = loser
        },
        updateGameObject(state,object){
            state.gameObject = object
        },
        updateGameMap(state,game){
            state.a_id = game.a_id
            state.b_id = game.b_id
            state.a_sx = game.a_sx
            state.a_sy = game.a_sy
            state.b_sx = game.b_sx
            state.b_sy = game.b_sy
            state.gameMap = game.map
        },
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
       
    },
    actions: {
       
    },
    modules: {
    }
}