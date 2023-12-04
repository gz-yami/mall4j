import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import moment from 'moment'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from '@/router'
import locale from 'element-plus/lib/locale/lang/zh-cn'
import Avue from '@smallwei/avue'
import '@smallwei/avue/lib/index.css'

// 全局样式
import '@/styles/index.scss'
// svg
import 'virtual:svg-icons-register'
import svgIcon from '@/icons/SvgIcon.vue'
moment.locale('zh-cn', {
  longDateFormat: {
    LT: 'HH:mm',
    LTS: 'HH:mm:ss',
    L: 'YYYY-MM-DD',
    LL: 'YYYY-MM-DD HH:mm:ss'
  },
  week: {
    // GB/T 7408-1994《数据元和交换格式·信息交换·日期和时间表示法》与ISO 8601:1988等效
    dow: 1, // 星期一， 是一个星期的第一天
    doy: 4 // 1月4日所在的的一周是一年的第一周
  }
})
const app = createApp(App)

// router
app.use(router)
// pinia
const pinia = createPinia()
app.use(pinia)

app.component('SvgIcon', svgIcon)

// Avue
app.use(Avue)
// element-plus
app.use(ElementPlus, { locale })
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
