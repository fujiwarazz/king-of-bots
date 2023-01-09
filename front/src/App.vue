<template>
  <navBar/>
  <router-view v-if="isRouterAlive"/>
</template>
<script>
import navBar from '@/components/navBar.vue'
// import request from './util/request'
import { ref ,provide,nextTick} from 'vue'
export default {
  components:{
    navBar
  },
  name:'app',
  setup(){
    const isRouterAlive = ref(true);
    const reload = () => {
      isRouterAlive.value = false;
      nextTick(() => {
        isRouterAlive.value = true;
      });
    }; 
    provide("reload", reload);
    return {
      isRouterAlive,
    };
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
