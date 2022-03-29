import Vue from 'vue'
import ElementUI from 'element-ui'
import App from '@/App'
import router from '@/router'                 // api: https://github.com/vuejs/vue-router
import store from '@/store'                   // api: https://github.com/vuejs/vuex
import VueCookie from 'vue-cookie'            // api: https://github.com/alfhen/vue-cookie
import '@/icons'                              // api: http://www.iconfont.cn/
import '@/element-ui-theme/style.css'
import Avue from '@smallwei/avue'             // api: https://avue.top
import '@smallwei/avue/lib/index.css'
import '@/assets/scss/index.scss'
import httpRequest from '@/utils/httpRequest' // api: https://github.com/axios/axios
import { isAuth } from '@/utils'
// import cloneDeep from 'lodash/cloneDeep'

Vue.use(Avue)
Vue.use(VueCookie)
Vue.use(ElementUI)
Vue.config.productionTip = false

// 挂载全局
Vue.prototype.$http = httpRequest // ajax请求方法
Vue.prototype.isAuth = isAuth     // 权限方法

// 保存整站vuex本地储存初始状态
// process.env.VUE_APP_RESOURCES_URL['storeState'] = cloneDeep(store.state)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
