<template>
  <div>
    <nav class="navbar navbar-expand-lg  navbar-light bg-light">
      <div class="container">
        <router-link class="navbar-brand" :to="{name:'dashbord'}">King of Bots</router-link>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarText"
          aria-controls="navbarText"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item" >
               <router-link  class="nav-link" :class="name == 'combat'? 'nav-link active':'nav-link'" :to="{name:'combat'}">对战</router-link>
            </li>
            <li class="nav-item">
              <router-link  class="nav-link"  :class="name == 'record'? 'nav-link active':'nav-link'"  :to="{name:'record'}">对局列表</router-link>

             </li>
            <li class="nav-item">
              <router-link  class="nav-link" :class="name == 'rank'? 'nav-link active':'nav-link'"   :to="{name:'rank'}">排行榜</router-link>

             </li>
          </ul>
          <ul class="navbar-nav ">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle"  
              aria-current="page" role="button" data-bs-toggle="dropdown" aria-expanded="false" href="#">{{nickName}}</a>
 
          <ul class="dropdown-menu">
            <li><router-link class="dropdown-item"  :class="name == 'userBot'? 'nav-link active':'nav-link'"  :to="{name:'userBot'}">myBots</router-link></li>
            <li><router-link class="dropdown-item"   :class="name == 'userProfile'? 'nav-link active':'nav-link'"  :to="{name:'userProfile'}">myProfile</router-link></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#">Quit</a></li>
          </ul>
        </li>
          </ul>
         </div>
      </div>
    </nav>
  </div>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed ,ref} from 'vue';
export default {
 setup(){
  let route = useRoute();
  let name = computed(()=>route.name)
  let nickName = ref('')
  if(localStorage.getItem('user')!=null){
    nickName.value = JSON.parse(localStorage.getItem('user')).nickname
  }else if(sessionStorage.getItem('user')!=null){
    nickName.value = JSON.parse(sessionStorage.getItem('user')).nickname
  }else{
    nickName.value ="游客"
  }

  return{
    name,
    nickName
  }
 }
};
</script>

<style scoped>  

</style>
