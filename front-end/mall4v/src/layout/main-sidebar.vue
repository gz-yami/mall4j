<template>
  <aside class="site-sidebar">
    <div class="menu-mod">
      <el-scrollbar class="menu-left">
        <ul>
          <li>
            <div
              :class="{
                'menu-left-active': selectLeftId === '',
                'menu-left-item1': 'language' === 'English',
                'menu-left-item': 'language' !== 'English'
              }"
              @click="toHome()"
            >
              <svg-icon
                icon-class="icon-shouye"
                style="font-size: 16px; margin-right: 3px !important"
              />
              <span style="font-size: 14px">首页</span>
            </div>
          </li>
        </ul>
        <ul>
          <template
            v-for="menu in menuList"
            :key="menu.menuId"
          >
            <li
              v-if="!menu.hidden"
              class="menu-left-active"
            >
              <div
                v-if="menu.list"
                :class="[
                  'menu-left-item',
                  {'menu-left-active': selectLeftId === menu.menuId}
                ]"
                @click="expandMenu(menu)"
              >
                <svg-icon
                  v-if="menu.icon"
                  :icon-class="`icon-${menu.icon}`"
                  style="font-size: 16px; margin-right: 3px !important"
                />
                <span
                  class="item-text"
                  :title="menu.name.length > 4 ? menu.name : ''"
                  style="font-size: 14px"
                >{{ menu.name }}</span>
              </div>
              <div
                v-else
                :class="{
                  'menu-left-active': selectLeftId === menu.menuId,
                  'menu-left-item1': '语言' === 'English',
                  'menu-left-item': '语言' !== 'English'
                }"
                @click="gotoRouteHandle(menu)"
              >
                <svg-icon
                  v-if="menu.icon"
                  :icon-class="menu.icon || ''"
                  style="font-size: 16px; margin-right: 3px !important"
                  class="site-sidebar__menu-icon"
                />
                <span
                  class="item-text"
                  :title="menu.name.length > 4 ? menu.name : ''"
                  style="font-size: 14px"
                >{{ menu.name }}</span>
              </div>
            </li>
          </template>
        </ul>
      </el-scrollbar>
      <SubMenu
        v-if="!sidebarFold"
        :key="selectLeftId"
        class="menu-right-con"
        :expand-menu="expandMenuList"
      />
    </div>
  </aside>
</template>
<script setup>
import SubMenu from './main-sidebar-sub-menu.vue'
const route = useRoute()
const router = useRouter()
const commonStore = useCommonStore()
const dynamicMenuRoutes = ref([])
const expandMenuList = ref([])

const sidebarFold = computed(() => commonStore.sidebarFold)
const menuList = computed({
  get: () => {
    return commonStore.menuList
  },
  set: val => {
    commonStore.updateMenuList(val)
  }
})

const selectLeftId = computed({
  get: () => {
    handleRightRoute(commonStore.selectLeftId)
    return commonStore.selectLeftId || ''
  }
})

onBeforeMount(() => {
  menuList.value = JSON.parse(sessionStorage.getItem('menuList') || '[]')
  dynamicMenuRoutes.value = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
  routeHandle(route)
  if (selectLeftId.value) {
    handleRightRoute(selectLeftId.value)
  }
})

const handleRightRoute = selectLeftId => {
  menuList.value.forEach(item => {
    if (selectLeftId === item.menuId) {
      expandMenu(item, true)
    }
  })
}

const toHome = () => {
  router.push({ name: 'home' })
  expandMenuList.value = []
  sessionStorage.setItem('isExpand', '0')
  commonStore.updateSidebarFold(true)
  commonStore.updateSelectLeftId('')
  commonStore.updateSelectRightId('')
}

const routeHandle = route => {
  if (route.name === 'home') {
    expandMenuList.value = []
    sessionStorage.setItem('isExpand', '0')
    commonStore.updateSelectLeftId('')
    commonStore.updateSelectRightId('')
  }
}

const gotoRouteHandle = menu => {
  if (router.history.current.name === menu.url) {
    expandMenuList.value = []
    commonStore.updateSidebarFold(true)
    sessionStorage.setItem('isExpand', '0')
    commonStore.updateSelectRightId('')
    commonStore.updateSelectLeftId(menu.menuId || '')
    return
  }
  if (menu.name === '消息' || menu.name === 'Message') {
    sessionStorage.setItem('isExpand', '0')
    window.open(location.href.split('#')[0] + '#/imBox', '_blank', 'noopener,noreferrer')
  } else {
    expandMenuList.value = []
    commonStore.updateSidebarFold(true)
    sessionStorage.setItem('isExpand', '0')
    commonStore.updateSelectRightId('')
    commonStore.updateSelectLeftId(menu.menuId || '')
    router.push({ name: menu.url })
  }
}

const expandMenu = menu => {
  expandMenuList.value = menu.list || []
  commonStore.updateSidebarFold(menu.list === null)
  const id1 = commonStore.selectLeftId
  commonStore.updateSelectLeftId(menu.menuId || '')
  const id2 = commonStore.selectLeftId
  if (menu.list) {
    sessionStorage.setItem('isExpand', '1')
  }
  if (id1 !== id2) {
    routeJump(menu)
  }
}

const routeJump = menu => {
  const routes = menu.list
  for (let i = 0; i < routes.length; i++) {
    if (!routes[i].hidden && !routes[i].list) {
      router.push({ name: routes[i].url })
      break
    } else if (routes[i].list) {
      let flag = false
      for (let j = 0; j < routes[i].list.length; j++) {
        if (!routes[i].list[j].hidden) {
          router.push({ name: routes[i].list[j].url })
          flag = true
          break
        }
      }
      if (flag) {
        break
      }
    }
  }
}
</script>

<style scoped>
.menu-mod {
  display: flex;
}
.menu-right-con {
  position: absolute;
  z-index: 1;
  left: 100px;
}
.menu-left {
  background: #222222;
  color: #ffffff !important;
  width: 100px;
  height: calc(100vh - 50px);
  overflow-y: auto;
}
.menu-mod .menu-left-item {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.menu-mod .menu-left-item1 {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  text-align: left;
  padding-left: 12px;
}
.menu-mod .menu-left ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  width: 100px;
  text-align: center;
}
.menu-mod .menu-right ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  width: 130px;
  text-align: center;
}
.menu-left li {
  background: #222222;
  color: #fff;
  height: 40px;
  cursor: pointer;
  font-size: 14px;
  stroke: #fff !important;
}

.menu-right li {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  color: #333333;
}

/* 鼠标移动到选项上修改背景颜色 */
.menu-left li:hover {
  background-color: #ffffff;
  color: #155bd4;
  stroke: #155bd4 !important;
}
/* 鼠标移动到选项上修改背景颜色 */
.menu-right li:hover {
  background-color: rgba(21, 91, 212, 0.1);
  color: #155bd4;
}
.menu-right li:hover {
  background-color: rgba(21, 91, 212, 0.1);
  color: #155bd4;
}
.menu-left-active {
  background-color: #ffffff;
  color: #155bd4;
  stroke: #155bd4 !important;
}
.item-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  max-width: 70px;
}
</style>
