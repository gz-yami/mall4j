<template>
  <div class="login">
    <div class="login-box">
      <div class="top">
        <div class="logo">
          <img
            src="~@/assets/img/login-logo.png"
            alt=""
          >
        </div>
      </div>
      <div class="mid">
        <el-form
          ref="dataFormRef"
          :model="dataForm"
          :rules="dataRule"
          status-icon
          @keyup.enter="dataFormSubmit()"
        >
          <el-form-item prop="userName">
            <el-input
              v-model="dataForm.userName"
              class="info"
              placeholder="帐号"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="dataForm.password"
              class="info"
              type="password"
              placeholder="密码"
            />
          </el-form-item>
          <el-form-item>
            <div class="item-btn">
              <input
                type="button"
                value="登录"
                @click="dataFormSubmit()"
              >
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div class="bottom">
        Copyright © 2019 广州市蓝海创新科技有限公司
      </div>
    </div>
    <Verify
      ref="verifyRef"
      :captcha-type="'blockPuzzle'"
      :img-size="{width:'400px',height:'200px'}"
      @success="login"
    />
  </div>
</template>

<script setup>
import { encrypt } from '@/utils/crypto'
import { getUUID } from '@/utils'
import Verify from '@/components/verifition/Verify.vue'
import cookie from 'vue-cookies'

const dataForm = ref({
  userName: '',
  password: '',
  uuid: '',
  captcha: ''
})
const dataRule = {
  userName: [
    { required: true, message: '帐号不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '验证码不能为空', trigger: 'blur' }
  ]
}

onBeforeUnmount(() => {
  document.removeEventListener('keyup', handerKeyup)
})
onMounted(() => {
  getCaptcha()
  document.addEventListener('keyup', handerKeyup)
})
const handerKeyup = (e) => {
  const keycode = document.all ? event.keyCode : e.which
  if (keycode === 13) {
    this.dataFormSubmit()
  }
}

const verifyRef = ref(null)
const dataFormRef = ref(null)
let isSubmit = false
/**
 * 提交表单
 */
const dataFormSubmit = () => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      verifyRef.value?.show()
    }
  })
}

const router = useRouter()
const login = (verifyResult) => {
  if (isSubmit) {
    return
  }
  isSubmit = true
  http({
    url: http.adornUrl('/adminLogin'),
    method: 'post',
    data: http.adornData({
      userName: dataForm.value.userName,
      passWord: encrypt(dataForm.value.password),
      captchaVerification: verifyResult.captchaVerification
    })
  }).then(({ data }) => {
    cookie.set('Authorization', data.accessToken)
    router.replace({ name: 'home' })
  }).catch(() => {
    isSubmit = false
  })
}

/**
 * 获取验证码
 */
const getCaptcha = () => {
  dataForm.value.uuid = getUUID()
}

</script>

<style lang="scss" scoped>
.login {
  width: 100%;
  height: 100%;
  background: url('../../../assets/img/login-bg.png') no-repeat;
  background-size: cover;
  position: fixed;
  .login-box {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    height: 100%;
    padding-top: 10%;
    .top {
      margin-bottom: 30px;
      text-align: center;
      .logo {
        font-size: 0;
        max-width: 50%;
        margin: 0 auto;
      }
      &:deep(.company) {
        font-size: 16px;
        margin-top: 10px;
      }
    }
    .mid {
      font-size: 14px;
      .item-btn {
        width: 410px;
        margin-top: 20px;
        input {
          border: 0;
          width: 100%;
          height: 40px;
          background: #1f87e8;
          color: #fff;
          border-radius: 3px;
        }
      }
    }
    .bottom {
      position: absolute;
      bottom: 10%;
      width: 100%;
      color: #999;
      font-size: 12px;
      text-align: center;
    }
  }
}
.info {
  width: 410px;
}
:deep(.login-captcha) {
  height: 40px;
}
</style>
