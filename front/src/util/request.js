import axios from 'axios'

// 创建axios
const request = axios.create({
    
  baseURL: 'http://localhost:9999', // api的base_url
  timeout: 20000,  // 请求超时时间

})
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json'

    let token_value =""

    if(localStorage.getItem('token')){
        token_value = JSON.parse(localStorage.getItem('token'))
    }else if(sessionStorage.getItem('token')){
        token_value = JSON.parse(sessionStorage.getItem('token'))
    }
    if(token_value!=null){
        config.headers['kob-token']=token_value
    }
    console.log(token_value);
    return config;
}, error => {
    return Promise.reject(error);
})

request.interceptors.response.use(function (response) {

    return response.data;
}, function (error) {
    return Promise.reject(error);
});

export default request;
