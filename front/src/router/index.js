import { createRouter, createWebHistory } from 'vue-router'
import dashbord from '@/views/dashbord/dashbordView.vue'
import combat from '@/views/combat/combatView.vue'
import record from '@/views/record/recordView.vue'
import rank from '@/views/rank/rankView.vue'
import userBot from '@/views/user/bots/botsView.vue'
import userProfile from '@/views/user/profile/profileView.vue'
import notFound from '@/views/error/notFound.vue'
import UserLogin from '@/views/user/account/UserLogin.vue'
import UserRegister from '@/views/user/account/UserRegister.vue'

import nprogress from 'nprogress'
import 'nprogress/nprogress.css'

const routes = [
  {
    path:'/',
    name:'index',
    redirect:'home',
    meta:{
      icon:'indeex'
    }
  },
  {
    path:'/home',
    name:'dashbord',
    component: dashbord
  },
  {
    path:'/combat',
    name:'combat',
    component:combat
  },
  {
    path:'/record',
    name:'record',
    component:record
  },
  {
    path:'/rank',
    name:'rank',
    component:rank
  },
  {
    path:'/user/bot',
    name:'userBot',
    component:userBot
  },
  {
    path:'/user/profile',
    name:'userProfile',
    component:userProfile
  },
  {
    path:'/user/account/login',
    name:'userLogin',
    component:UserLogin
  },
  {
    path:'/user/account/register',
    name:'userRegister',
    component:UserRegister
  },

  {
    path:'/404',
    name:'not found',
    component:notFound
  },
  {
    path:'/:catchAll(.*)',
    redirect:'404'
  }
 
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to, from, next) => {
  nprogress.start()
  next()
})

router.afterEach(() => {
  nprogress.done()
})
export default router
