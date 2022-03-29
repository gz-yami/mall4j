import Vue from 'vue'
import Vuex from 'vuex'
import cloneDeep from 'lodash/cloneDeep'
import common from './modules/common'
import user from './modules/user'
import prod from './modules/prod'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    common,
    user,
    prod
  },
  mutations: {
    // 重置vuex本地储存状态
    resetStore (state) {
      Object.keys(state).forEach((key) => {
        state[key] = cloneDeep(process.env.VUE_APP_RESOURCES_URL['storeState'][key])
      })
    }
  },
  strict: process.env.NODE_ENV !== 'production'
})
