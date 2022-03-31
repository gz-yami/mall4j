import Vue from 'vue'
import router from '@/router'
import store from '@/store'

/**
 * 获取uuid
 */
export function getUUID () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
  })
}

/**
 * 是否有权限
 * @param {*} key
 */
export function isAuth (key) {
  let authorities = JSON.parse(sessionStorage.getItem('authorities') || '[]')
  if (authorities.length) {
    for (const i in authorities) {
      const element = authorities[i]
      if (element === key) {
        return true
      }
    }
  }
  return false
}

/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function treeDataTranslate (data, id = 'id', pid = 'parentId') {
  var res = []
  var temp = {}
  for (var i = 0; i < data.length; i++) {
    temp[data[i][id]] = data[i]
  }
  for (var k = 0; k < data.length; k++) {
    if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
      if (!temp[data[k][pid]]['children']) {
        temp[data[k][pid]]['children'] = []
      }
      if (!temp[data[k][pid]]['_level']) {
        temp[data[k][pid]]['_level'] = 1
      }
      data[k]['_level'] = temp[data[k][pid]]._level + 1
      temp[data[k][pid]]['children'].push(data[k])
    } else {
      res.push(data[k])
    }
  }
  return res
}

/**
 * 将数组中的parentId列表取出，倒序排列
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function idList (data, val, id = 'id', children = 'children') {
  let res = []
  idListFromTree(data, val, res, id)
  return res
}

/**
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
function idListFromTree (data, val, res = [], id = 'id', children = 'children') {
  for (let i = 0; i < data.length; i++) {
    const element = data[i]
    if (element[children]) {
      if (idListFromTree(element[children], val, res, id, children)) {
        res.push(element[id])
        return true
      }
    }
    if (element[id] === val) {
      res.push(element[id])
      return true
    }
  }
}

/**
 * 清除登录信息
 */
export function clearLoginInfo () {
  Vue.cookie.delete('Authorization')
  store.commit('resetStore')
  router.options.isAddDynamicMenuRoutes = false
}
