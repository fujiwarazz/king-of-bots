import request from '../util/request'
export default{
    state: {
        userId:"",
        nickname:"",
        token:"",
        is_login:false,
        avatar:''
    },
    getters: {
        getUserToken:state=>state.token

    },
    mutations: {
        updateUser(state,user){
            state.userId = user.userId;
            state.nickname = user.nickname;
            state.is_login = user.is_login;
            state.avatar = user.avatar;
        },
        updateToken(state,token){
            state.token = token;
            state.is_login = true
        },
        logout(state){
            state.id = '';
            state.nickname = ''
            state.is_login = false;
            state.avatar =''
        }

    },
    actions: {
        login(context,data){
            request({
                url:'/user/login',
                method:'post',
                data:{
                    nickname:data.nickname,
                    password:data.password
                }
            }).then((res) => {
                if(res.code==200){
                    context.commit('updateToken',res.data)
                    //hook
                    data.success(res);
                }else{
                    data.error(res);
                }
            })
        },
        getInfo(context,data){
            let token = JSON.stringify(context.state.token)
            request({
                url:'/user/account/info',
                method:'get',
                headers:{
                    kob_token:JSON.parse(token)
                }
                
            }).then((res) => {
                if(res.code==200){
                    //hook
                context.commit("updateUser",{
                    ...res.data,
                    is_login :true,
                });
                data.success(res);
                }else{
                    data.error(res)
                
                }
            })
        },
        logout(context,data){
            request({
                url:'/user/account/logout',
                method:'post',
            }).then((res) => {
                if(res.code==200){
                    if (localStorage.getItem("user") != null) {
                        localStorage.removeItem("user");
                        localStorage.removeItem("token");
                      } else if (sessionStorage.getItem("user") != null) {
                        sessionStorage.removeItem("user");
                        localStorage.removeItem("token");
                      }
                      data.success();
                }
                
            })
            context.commit("logout")
        }
    },
    modules: {
    }
}