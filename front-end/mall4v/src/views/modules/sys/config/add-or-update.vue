<template>
  <el-dialog
    v-model="visible"
    :title="!dataForm.id ? '新增' : '修改'"
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
        label="参数名"
        prop="paramKey"
      >
        <el-input
          v-model="dataForm.paramKey"
          placeholder="参数名"
        />
      </el-form-item>
      <el-form-item
        label="参数值"
        prop="paramValue"
      >
        <el-input
          v-model="dataForm.paramValue"
          placeholder="参数值"
        />
      </el-form-item>
      <el-form-item
        label="备注"
        prop="remark"
      >
        <el-input
          v-model="dataForm.remark"
          placeholder="备注"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
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
const dataForm = reactive({
  id: 0,
  paramKey: '',
  paramValue: '',
  remark: ''
})
const dataRule = {
  paramKey: [
    { required: true, message: '参数名不能为空', trigger: 'blur' },
    { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的参数名', trigger: 'blur' }
  ],
  paramValue: [
    { required: true, message: '参数值不能为空', trigger: 'blur' },
    { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的参数值', trigger: 'blur' }
  ]
}
const dataFormRef = ref(null)
const init = (id) => {
  dataForm.id = id || 0
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
    if (dataForm.id) {
      http({
        url: http.adornUrl(`/sys/config/info/${dataForm.id}`),
        method: 'get',
        params: http.adornParams()
      }).then(({ data }) => {
        dataForm.paramKey = data.paramKey
        dataForm.paramValue = data.paramValue
        dataForm.remark = data.remark
      })
    }
  })
}
defineExpose({ init })
/**
 * 表单提交
 */
const onSubmit = Debounce(() => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      http({
        url: http.adornUrl('/sys/config'),
        method: dataForm.id ? 'put' : 'post',
        data: http.adornData({
          id: dataForm.id || undefined,
          paramKey: dataForm.paramKey,
          paramValue: dataForm.paramValue,
          remark: dataForm.remark
        })
      }).then(() => {
        ElMessage({
          message: '操作成功',
          type: 'success',
          duration: 1500,
          onClose: () => {
            visible.value = false
            emit('refreshDataList')
          }
        })
      })
    }
  })
})
</script>
