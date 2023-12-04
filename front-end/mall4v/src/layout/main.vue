<template>
  <div
    v-loading.fullscreen.lock="loading"
    class="site-wrapper"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    :element-loading-text="'拼命加载中'"
  >
    <template v-if="!loading">
      <MainNavbar />
      <MainSidebar />
      <div
        class="site-content__wrapper"
        :style="{ 'min-height': documentClientHeight + 'px' }"
      >
        <main-content />
      </div>
    </template>
  </div>
</template>

<script setup>
import MainNavbar from './main-navbar.vue'
import MainSidebar from './main-sidebar.vue'
import MainContent from './main-content.vue'
onBeforeMount(() => {
  getUserInfo()
})

const commonStore = useCommonStore()
const documentClientHeight = computed(() => commonStore.documentClientHeight)
const userStore = useUserStore()
const sidebarFold = computed(() => commonStore.sidebarFold)
onMounted(() => {
  resetDocumentClientHeight()
})
const resetDocumentClientHeight = () => {
  commonStore.documentClientHeight = document.documentElement.clientHeight
  window.onresize = () => {
    commonStore.documentClientHeight = document.documentElement.clientHeight
  }
}

const loading = ref(true)
// 获取当前管理员信息
const getUserInfo = () => {
  http({
    url: http.adornUrl('/sys/user/info'),
    method: 'get',
    params: http.adornParams()
  }).then(({ data }) => {
    loading.value = false
    userStore.userId = data.userId
    userStore.name = data.username
    userStore.mobile = data.mobile
    userStore.shopId = data.shopId
    userStore.userId = data.userId
  }).catch(() => {})
}
</script>
