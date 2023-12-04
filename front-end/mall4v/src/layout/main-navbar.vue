<template>
  <div>
    <nav class="site-navbar">
      <!--左侧-->
      <div
        class="site-navbar-header"
        :style="{ 'margin-right': sidebarFold ? 0 : '20px' }"
      >
        <!--        <img-->
        <!--          class="menu-image-logo"-->
        <!--          :src="configuration.bsTopBarIcon"-->
        <!--          alt="logo"-->
        <!--        >-->
        <span
          v-if="!sidebarFold"
          class="site-navbar-lg"
        >
          mall4j建站后台
        </span>
        <span
          v-else
          class="site-navbar-mini"
          :style="fontCloseSize"
        >
          mall4j
        </span>
      </div>
      <!--右侧数据-->
      <div class="site-navbar-content">
        <div class="navbar-content-left">
          <svg-icon
            class="left-item"
            icon-class="icon-zhedie"
            @click="setSidebarFold"
          />
        </div>

        <div class="navbar-content-right">
          <el-dropdown
            class="content-right-item"
            :show-timeout="0"
            placement="bottom"
          >
            <span class="el-dropdown-link">{{ userName }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="updatePasswordHandle">
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item @click="logoutHandle">
                  退出
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <!-- 弹窗, 修改密码 -->
      <UpdatePassword
        v-if="updatePassowrdVisible"
        ref="updatePassowrdRef"
      />
    </nav>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
import UpdatePassword from './main-navbar-update-password.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const userName = computed(() => userStore.name)
const fontCloseSize = reactive({
  fontSize: '16px'
})
const commonStore = useCommonStore()
const sidebarFold = computed(() => commonStore.sidebarFold)
const setSidebarFold = () => {
  const len = commonStore.selectMenu.length
  const flag = sessionStorage.getItem('isExpand')
  if ((route.path === '/home' || len === 1) && flag === '0') {
    commonStore.updateSidebarFold(true)
  } else {
    const foldFlag = sidebarFold.value
    commonStore.updateSidebarFold(!foldFlag)
  }
}

const logoutHandle = () => {
  ElMessageBox.confirm('确定进行[退出]操作?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http({
      url: http.adornUrl('/logOut'),
      method: 'post',
      data: http.adornData()
    }).then(() => {
      clearLoginInfo()
      router.push({ name: 'login' })
    })
  })
}

const updatePassowrdVisible = ref(false)
const updatePassowrdRef = ref(null)
/**
 * 修改密码
 */
const updatePasswordHandle = () => {
  updatePassowrdVisible.value = true
  nextTick(() => {
    updatePassowrdRef.value?.init()
  })
}
</script>

<style lang="scss" scoped>
.site-navbar {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  color: #333333;
  border-bottom: 1px solid #ebedf0;
  .site-navbar-header {
    display: flex;
    align-items: center;
    margin-left: 20px;
    color: #333;
    font-weight: 700;
    height: 50px;
    line-height: 50px;
    .site-navbar-lg {
      font-size: 16px;
      word-break: break-all;
      word-wrap: break-word;
    }
    .site-navbar-lg,
    .site-navbar-mini {
      margin: 0 5px;
    }
  }
  .site-navbar-content {
    flex: 1;
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding: 0 20px;
    font-size: 18px;
    align-items: center;
    .navbar-content-left {
      flex: 1;
      .left-item {
        cursor: pointer;
      }
    }

    .navbar-content-right {
      display: flex;
    }
  }
}
//.menu-image-logo {
//  object-fit: contain;
//  height: 18px;
//  width: 59px;
//  margin-right: 10px;
//}
</style>
