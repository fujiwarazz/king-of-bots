import axios from 'axios'

// 创建axios
const request = axios.create({
  baseURL: 'http://localhost:8160', // api的base_url
  timeout: 20000 // 请求超时时间
})
request.interceptors.request.use(config => {
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
