import CryptoJS from 'crypto-js'
// 加密
const keyStr = '-mall4j-password' // 解密用的key
export function encrypt (word) {
  const time = Date.now()

  const key = CryptoJS.enc.Utf8.parse(keyStr)
  const srcs = CryptoJS.enc.Utf8.parse(time + word) // 加密方式: 时间戳 + 密文
  const encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7})
  return encrypted.toString()
}
