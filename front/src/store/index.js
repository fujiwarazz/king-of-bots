import { createStore } from 'vuex'
import ModuleUser from './user'
import ModuleCombat from './combat'
import ModuleRecord from './record'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user:ModuleUser,
    combat:ModuleCombat,
    record:ModuleRecord
  }
})
