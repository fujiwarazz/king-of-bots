<template>
  <navBar/>
  <router-view/>
</template>
<script>
import navBar from '@/components/navBar.vue'
import request from './util/request'
import { ref } from 'vue'
export default {
  components:{
    navBar
  },
  name:'app',
  setup(){
    let token = ref('');
    request({
      url:"http://localhost:9999/user/login",
      method:"POST",
      data:{
        "nickname":"peelsannaw",
        "password":"admin"
      }
    }).then(res=>{
      console.log(res)
      token = res.data.token
    })

    request({
      url:"http://localhost:9999/user/account/info",
      method:"get",
      headers:{
        "kob-token":token
      }
    }).then(res=>{
      console.log(res)
    })
     return {token}
  }
}

</script>
<style>
body{
  background-image: url(@/assets/images/background.jpg);
  background-size: cover;
  margin: 0;

}
#nprogress .bar {
      background: blue !important;  
    }
</style>
