<template>
  <el-dialog
    v-model="visible"
    :modal="false"
    title="选择发货地址"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="80px"
      @keyup.enter="onSubmit()"
    >
      <el-form-item label="快递公司">
        <el-select
          v-model="dataForm.dvyId"
          placeholder="请选择"
        >
          <el-option
            v-for="item in dataForm.dvyNames"
            :key="item.dvyId"
            :label="item.dvyName"
            :value="item.dvyId"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        label="快递单号"
        prop="dvyFlowId"
      >
        <el-input
          v-model="dataForm.dvyFlowId"
          controls-position="right"
          :min="0"
          label="快递单号"
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
const emit = defineEmits(['refreshDataList'])

// eslint-disable-next-line no-unused-vars
const validDvyFlowId = (rule, value, callback) => {
  if (!value.trim()) {
    callback(new Error('不能为空'))
  } else {
    callback()
  }
}
const dataRule = {
  dvyFlowId: [
    { required: true, message: '不能为空', trigger: 'blur' },
    { validator: validDvyFlowId, trigger: 'blur' }
  ]
}

const visible = ref(false)
const dataForm = reactive({
  dvyId: '',
  dvyFlowId: 0,
  dvyNames: [],
  orderNumber: 0
})

const init = (orderNumber, dvyId, dvyFlowId) => {
  visible.value = true
  dataForm.orderNumber = orderNumber || ''
  dataForm.dvyId = dvyId || ''
  dataForm.dvyFlowId = dvyFlowId || ''
  http({
    url: http.adornUrl('/admin/delivery/list'),
    method: 'get',
    params: http.adornParams()
  }).then(({ data }) => {
    dataForm.dvyNames = data
  })
}
defineExpose({ init })

const dataFormRef = ref(null)
/**
 * 表单提交
 */
const onSubmit = () => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      http({
        url: http.adornUrl('/order/order/delivery'),
        method: 'put',
        data: http.adornData({
          orderNumber: dataForm.orderNumber,
          dvyId: dataForm.dvyId,
          dvyFlowId: dataForm.dvyFlowId
        })
      })
        .then(() => {
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
}

</script>
