<template>
  <div class="menu-mod">
    <div v-if="expandMenu.list">
      <el-sub-menu
        v-if="!item.hidden"
        :index="expandMenu.menuId + ''"
      >
        <template #title>
          <span
            style="font-size: 14px"
            :title="expandMenu.name.length > 4 ? expandMenu.name : ''"
          >{{ expandMenu.name }}</span>
        </template>
        <el-menu-item-group class="menu-right-el-item">
          <template v-for="menu in expandMenu.list">
            <el-menu-item
              v-if="!menu.hidden"
              :key="menu.menuId"
              style="
                font-size: 14px !important;
                line-height: 40px;
                padding-left: 30px !important;
                padding-right: 10px !important;
              "
              class="menu-right-el-item is-active item-text"
              :class="{ 'menu-active': selectRightId === menu.menuId }"
              @click="gotoRouteHandle(menu)"
            >
              <span :title="menu.name.length > 4 ? menu.name : ''">{{ menu.name }}</span>
              <SubMenuItem
                v-if="menu.list"
                :expand-menu="menu"
              />
            </el-menu-item>
          </template>
        </el-menu-item-group>
      </el-sub-menu>
    </div>
    <div v-else>
      <el-menu-item
        v-if="!expandMenu.hidden"
        :key="expandMenu.menuId"
        style="font-size: 14px !important; padding-left: 15px !important; line-height: 40px"
        class="menu-right-el-item is-active item-text"
        :class="{ 'menu-active': selectRightId === expandMenu.menuId }"
        @click="gotoRouteHandle(expandMenu)"
      >
        <span :title="expandMenu.name.length > 4 ? expandMenu.name : ''">{{
          expandMenu.name
        }}</span>
      </el-menu-item>
    </div>
  </div>
</template>

<script setup>
import SubMenuItem from './main-sidebar-sub-menu-item.vue'
const props = defineProps({
  expandMenu: {
    type: Object,
    default: () => {}
  },
  menuIndex: {
    type: String,
    default: ''
  }
})
const item = ref(props.expandMenu)

const selectRightId = computed(() => commonStore.selectRightId)

const commonStore = useCommonStore()
const router = useRouter()
// 监听路由
watch(
  () => router.currentRoute,
  route => {
    routeHandle(route)
  }
)
// 路由操作
const routeHandle = route => {
  if (route.meta.isTab) {
    commonStore.updateSelectRightId(route.meta.menuId || '')
  }
}

// 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
const gotoRouteHandle = menu => {
  if (router.currentRoute.value.name === menu.url) {
    return
  }
  router.push({ name: menu.url })
}
</script>
<style lang="scss" scoped>
.menu-mod {
  .menu-right-el-item {
    &.is-active {
      background-color: #ffffff;
      color: #333;
    }
    &.menu-active {
      background-color: #e7eefb;
      color: #155bd4;
    }
    :deep(.el-menu-item-group__title) {
      padding: 0;
    }
  }
  div .el-sub-menu__title span {
    display: inline-block;
    width: 85px;
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .el-sub-menu  .menu-right-el-item.el-menu-item span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
