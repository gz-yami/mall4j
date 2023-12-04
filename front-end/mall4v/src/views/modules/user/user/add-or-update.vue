<template>
  <el-dialog
    v-model="visible"
    :title="!dataForm.userId ? '新增' : '修改'"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="80px"
      @keyup.enter="onSubmit()"
    >
      <el-form-item
        label="用户头像"
        prop="pic"
      >
        <img
          v-if="dataForm.pic"
          :src="dataForm.pic"
          class="image"
          alt=""
        >
        <div v-else>
          无
        </div>
      </el-form-item>
      <el-form-item
        label="用户昵称"
        prop="nickName"
      >
        <el-input
          v-model="dataForm.nickName"
          :disabled="true"
          placeholder="用户昵称"
        />
      </el-form-item>
      <el-form-item
        label="状态"
        prop="status"
      >
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">
            禁用
          </el-radio>
          <el-radio :label="1">
            正常
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span
        class="dialog-footer"
      >
        <el-button @click="visible = false">取消</el-button>
        <el-button
          type="primary"
          @click="onSubmit()"
        >确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { Debounce } from '@/utils/debounce'
const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataForm = ref({
  userId: 0,
  nickName: '',
  pic: '',
  status: 1
})
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
const dataRule = {
  nickName: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ]
}

const dataFormRef = ref(null)
const init = (id) => {
  dataForm.value.userId = id || 0
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
  })
  if (dataForm.value.userId) {
    http({
      url: http.adornUrl(`/admin/user/info/${dataForm.value.userId}`),
      method: 'get',
      params: http.adornParams()
    })
      .then(({ data }) => {
        dataForm.value = data
      })
  }
}
defineExpose({ init })

/**
 * 表单提交
 */
const onSubmit = Debounce(() => {
  dataFormRef.value?.validate(valid => {
    if (valid) {
      http({
        url: http.adornUrl('/admin/user'),
        method: dataForm.value.userId ? 'put' : 'post',
        data: http.adornData({
          userId: dataForm.value.userId || undefined,
          nickName: dataForm.value.nickName,
          status: dataForm.value.status
        })
      })
        .then(() => {
          ElMessage({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              visible.value = false
              emit('refreshDataList', page)
            }
          })
        })
    }
  })
})

</script>
