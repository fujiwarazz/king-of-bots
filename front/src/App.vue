<template>
  <navBar/>
  <router-view/>
</template>
<script>
import navBar from '@/components/navBar.vue'
// import request from './util/request'
// import { ref } from 'vue'
export default {
  components:{
    navBar
  },
  name:'app',
  setup(){
   
  }
}

</script>
<style>
body{
  background-image: url(@/assets/images/background.jpg);
  background-size: cover;
  margin: 0;
  overflow-x:hidden;
  overflow-y:hidden;
  padding-right: 0px !important;/* 强制设置右侧填充为0，这样弹窗的代码就改不动它了 */
}

#nprogress .bar {
      background: blue !important;  
    }
</style>
