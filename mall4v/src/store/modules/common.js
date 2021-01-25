import router from '@/router'

export default {
  namespaced: true,
  state: {
    // 页面文档可视高度(随窗口改变大小)
    documentClientHeight: 0,
    // 导航条, 布局风格, defalut(默认) / inverse(反向)
    navbarLayoutType: 'default',
    // 侧边栏, 布局皮肤, light(浅色) / dark(黑色)
    sidebarLayoutSkin: 'dark',
    // 侧边栏, 折叠状态
    sidebarFold: false,
    // 侧边栏, 菜单
    menuList: [],
    menuActiveName: '',
    // 主入口标签页
    mainTabs: [],
    mainTabsActiveName: ''
  },
  mutations: {
    updateDocumentClientHeight (state, height) {
      state.documentClientHeight = height
    },
    updateSidebarFold (state, fold) {
      state.sidebarFold = fold
    },
    updateMenuList (state, list) {
      state.menuList = list
    },
    updateMenuActiveName (state, name) {
      state.menuActiveName = name
    },
    updateMainTabs (state, tabs) {
      state.mainTabs = tabs
    },
    updateMainTabsActiveName (state, name) {
      state.mainTabsActiveName = name
    },
    removeMainActiveTab (state) {
      state.mainTabs = state.mainTabs.filter(item => item.name !== state.mainTabsActiveName)
      if (state.mainTabs.length >= 1) {
        // 当前选中tab被删除
        router.push({ name: state.mainTabs[state.mainTabs.length - 1].name }, () => {
          state.mainTabsActiveName = state.mainTabs[state.mainTabs.length - 1].name
        })
      } else {
        state.menuActiveName = ''
        router.push({ name: 'home' })
      }
    }
  }
}
