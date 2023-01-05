import { createApp } from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
 import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import status from './assets/scripts/status'

const vuetify = createVuetify({
    components,
    directives,
})

const app = createApp(App).use(router).use(store).use(ElementPlus)
.use(vuetify)
app.config.globalProperties.$status = status
app.mount('#app')
