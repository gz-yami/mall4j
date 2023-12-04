/**
 * 邮箱
 * @param {*} s
 */
export function isEmail (s) {
  return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
}

/**
 * 手机号码
 * @param {*} s
 */
export function isMobile (s) {
  return /^1[0-9]{10}$/.test(s)
}

/**
 * 电话号码
 * @param {*} s
 */
export function isPhone (s) {
  return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s)
}

/**
 * URL地址
 * @param {*} s
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}

/**
 * qq
 * @param {*} s
 */
export function isQq (s) {
  return /[1-9][0-9]{4,14}/.test(s)
}

/**
 * 判断是否全为空格 只要有一个其他字符返回false
 * @param {String} str
 * @returns {Boolean}
 */
export function validNoEmptySpace (str) {
  const reg = /^\s+$/g
  return reg.test(str)
}
