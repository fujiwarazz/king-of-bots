import { createRouter, createWebHistory } from 'vue-router'
import dashbord from '@/views/dashbord/dashbordView.vue'
import combat from '@/views/combat/combatView.vue'
import record from '@/views/record/recordView.vue'
import recordContent from '@/views/record/recordContent.vue'

import rank from '@/views/rank/rankView.vue'
import userBot from '@/views/user/bots/botsView.vue'
import userProfile from '@/views/user/profile/profileView.vue'
import notFound from '@/views/error/notFound.vue'
import UserLogin from '@/views/user/account/UserLogin.vue'
import UserRegister from '@/views/user/account/UserRegister.vue'
import store from '../store/index'
import nprogress from 'nprogress'
import 'nprogress/nprogress.css'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/',
    name: 'index',
    redirect: 'home',
    meta: {
      needLogin:false
    }
  },
  {
    path: '/home',
    name: 'dashbord',
    component: dashbord,
    meta: {
      needLogin:false
    }
  },
  {
    path: '/combat',
    name: 'combat',
    component: combat,
    meta: {
      needLogin:true
    }
  },
  {
    path: '/record',
    name: 'record',
    component: record,
    meta: {
      needLogin:false
    }
  },
  {
    path: '/record/:recordId/',
    name: 'recordContent',
    component: recordContent,
    meta: {
      needLogin:false
    }
  },
  
  {
    path: '/rank',
    name: 'rank',
    component: rank,
    meta: {
      needLogin:false
    }
  },
  {
    path: '/user/bot',
    name: 'userBot',
    component: userBot,
    meta: {
      needLogin:true
    }
  },
  {
    path: '/user/profile',
    name: 'userProfile',
    component: userProfile,
    meta: {
      needLogin:true
    }
  },
  {
    path: '/user/account/login',
    name: 'userLogin',
    component: UserLogin,
    meta: {
      needLogin:false
    }
  },
  {
    path: '/user/account/register',
    name: 'userRegister',
    component: UserRegister,
    meta: {
      needLogin:false
    }
  },
  {
    path: '/404',
    name: 'not found',
    component: notFound
  },
  {
    path: '/:catchAll(.*)',
    redirect: '404'
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  nprogress.start()
  let token = localStorage.getItem('token') || sessionStorage.getItem('token')
  token = JSON.parse(token)
  if(token){
    store.commit('updateToken',JSON.stringify(token))
    store.dispatch('getInfo',{
      success(){},
      error(){
        ElMessage({
          message:'请登录!',
          type: 'warning',
          duration:1000,
        })
        next('/user/account/login')
      }
    })
  }else{
    if(to.meta.needLogin == true){
      next('/user/account/login')
    }
  }
  
  if(to.meta.needLogin==true && !store.state.user.is_login){
    ElMessage({
      message:'请登录!',
      type: 'warning',
      duration:1000,
    })
    next('/user/account/login')
  }else{
    next();
  }
  
})

router.afterEach(() => {
  nprogress.done()
})
export default router
