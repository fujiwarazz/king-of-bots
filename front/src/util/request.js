import axios from 'axios'
import router from '../router'
// 创建axios
const request = axios.create({
    
  baseURL: 'http://localhost:9999', // api的base_url
  timeout: 20000,  // 请求超时时间

})
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json'

    let token_value=null;
    if(localStorage.getItem('token')){
        token_value = JSON.parse(localStorage.getItem('token'))
    }else if(sessionStorage.getItem('token')){
        token_value = JSON.parse(sessionStorage.getItem('token'))
    }
    if(token_value!=null){
        config.headers['kob_token']=token_value
    }
    return config;
}, error => {
    return Promise.reject(error);
})

request.interceptors.response.use(function (response) {
    if(response.data.code===1){
        localStorage.removeItem('user')
        localStorage.removeItem('token')
        sessionStorage.removeItem('user')
        sessionStorage.removeItem('token')

        router.push("/user/account/login")
    }
    return response.data;
}, function (error) {
    return Promise.reject(error);
});

export default request;
