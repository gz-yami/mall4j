<template>
  <!-- 发货信息，用于导出代发货订单的excel交给快递公司 -->
  <el-dialog
    v-model="visible"
    :modal="false"
    title="请输入发货信息"
    :close-on-click-modal="false"
    width="38%"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="120px"
      @keyup.enter="onSubmit()"
    >
      <el-form-item
        label="发件人姓名"
        prop="consignmentName"
      >
        <el-input
          v-model="dataForm.consignmentName"
          controls-position="right"
          label="发件人姓名"
        />
      </el-form-item>
      <el-form-item
        label="发货人手机号"
        prop="consignmentMobile"
      >
        <el-input
          v-model="dataForm.consignmentMobile"
          controls-position="right"
          label="发货人手机号"
        />
      </el-form-item>
      <el-form-item
        label="发货地址"
        prop="consignmentAddr"
      >
        <el-input
          v-model="dataForm.consignmentAddr"
          controls-position="right"
          label="发货地址"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button
          @click="visible = false"
        >取消</el-button>
        <el-button
          type="primary"
          @click="onSubmit()"
        >确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
const emit = defineEmits(['inputCallback'])
const visible = ref(false)
const dataForm = reactive({
  consignmentName: '',
  consignmentMobile: '',
  consignmentAddr: ''
})
const dataRule = {
  consignmentName: [
    { required: true, message: '不能为空', trigger: 'blur' }
  ],
  consignmentMobile: [
    { required: true, message: '不能为空', trigger: 'blur' }
  ],
  consignmentAddr: [
    { required: true, message: '不能为空', trigger: 'blur' }
  ]
}

const dataFormRef = ref(null)
const init = () => {
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
  })
}
defineExpose({ init })

/**
 * 表单提交
 */
const onSubmit = () => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      visible.value = false
      emit('inputCallback', dataForm)
    }
  })
}
</script>
