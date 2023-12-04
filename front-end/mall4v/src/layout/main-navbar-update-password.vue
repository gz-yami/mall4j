<template>
  <el-dialog
    v-model="visible"
    :title="'修改密码'"
    :append-to-body="true"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="80px"
      @keyup.enter="onSubmit(dataFormRef)"
    >
      <el-form-item label="账号">
        <span>{{ userName }}</span>
      </el-form-item>
      <el-form-item
        label="原密码"
        prop="password"
      >
        <el-input
          v-model="dataForm.password"
          type="password"
        />
      </el-form-item>
      <el-form-item
        label="新密码"
        prop="newPassword"
      >
        <el-input
          v-model="dataForm.newPassword"
          type="password"
        />
      </el-form-item>
      <el-form-item
        label="确定密码"
        prop="confirmPassword"
      >
        <el-input
          v-model="dataForm.confirmPassword"
          type="password"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button
          type="primary"
          @click="onSubmit(dataFormRef)"
        >确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
const visible = ref(false)
const dataForm = reactive({
  password: '',
  newPassword: '',
  confirmPassword: ''
})
const dataFormRef = ref()
const init = () => {
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
  })
}
// eslint-disable-next-line no-unused-vars
const validateConfirmPassword = (rule, value, callback) => {
  if (dataForm.newPassword !== value) {
    callback(new Error('确认密码与新密码不一致'))
  } else {
    callback()
  }
}
const dataRule = reactive({
  password: [{ required: true, message: '原密码不能为空', trigger: 'blur' }],
  newPassword: [{ required: true, message: '新密码不能为空', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
})
const userStore = useUserStore()
const userName = computed(() => userStore.name)
const router = useRouter()
const onSubmit = async formEl => {
  if (!formEl) return
  await formEl.validate(valid => {
    if (valid) {
      http({
        url: http.adornUrl('/sys/user/password'),
        method: 'post',
        data: http.adornData({
          password: encrypt(dataForm.password),
          newPassword: encrypt(dataForm.newPassword)
        })
      }).then(() => {
        ElMessage({
          message: '操作?',
          type: 'success',
          duration: 1500,
          onClose: () => {
            visible.value = false
            nextTick(() => {
              clearLoginInfo()
              router.replace({ name: 'login' })
            })
          }
        })
      })
    }
  })
}

defineExpose({ init })
</script>
