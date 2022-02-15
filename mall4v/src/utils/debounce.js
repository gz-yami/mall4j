// 防抖 防止表单重复提交
export const Debounce = (fn, t) => {
  let delay = t || 300
  let timer
  return function () {
    let args = arguments
    if (timer) {
      clearTimeout(timer)
    }

    let callNow = !timer

    timer = setTimeout(() => {
      timer = null
    }, delay)

    if (callNow) fn.apply(this, args)
  }
}
