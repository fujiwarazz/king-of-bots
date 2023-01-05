<template>
  <navBar/>
  <router-view/>
</template>
<script>
import navBar from '@/components/navBar.vue'

export default {
  components:{
    navBar
  },
  name:'app',
  setup(){

     return {}
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
