<template>
  <el-dialog
    v-model="visible"
    :title="!dataForm.areaId ? '新增' : '修改'"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="100px"
      @keyup.enter="onSubmit()"
    >
      <el-form-item
        label="地区名称"
        prop="areaName"
      >
        <el-input
          v-model="dataForm.areaName"
          placeholder="请输入地区名称"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      <el-form-item
        label="上级地区"
        prop="parentId"
      >
        <el-cascader
          v-model="selectedOptions"
          expand-trigger="hover"
          :options="areaList"
          :props="categoryTreeProps"
          change-on-select
          filterable
          @change="handleChange"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button
          @click="visible = false"
        >
          取消
        </el-button>
        <el-button
          type="primary"
          @click="onSubmit()"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { treeDataTranslate } from '@/utils'
import { Debounce } from '@/utils/debounce'
const emit = defineEmits(['refreshDataList'])
const dataRule = reactive({
  areaName: [
    { required: true, message: '区域名称不能为空', trigger: 'blur' },
    { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的区域名称', trigger: 'blur' }
  ]
})

const visible = ref(false)
const areaList = ref([])
const dataForm = ref({
  areaId: '',
  areaName: null,
  parentId: null,
  level: null
})
const categoryTreeProps = reactive({
  value: 'areaId',
  label: 'areaName',
  checkStrictly: true
})
const selectedOptions = ref([])
const dataFormRef = ref(null)
const init = (areaId) => {
  dataForm.value.areaId = areaId || 0
  visible.value = true
  selectedOptions.value = []
  nextTick(() => {
    dataFormRef.value?.resetFields()
    if (dataForm.value.areaId) {
      http({
        url: http.adornUrl('/admin/area/info/' + dataForm.value.areaId),
        method: 'get',
        params: http.adornParams()
      })
        .then(({ data }) => {
          dataForm.value = data
          selectedOptions.value = dataForm.value.parentId
          categoryTreeProps.areaId = dataForm.value.areaId
          categoryTreeProps.areaName = dataForm.value.areaName
        })
    }
    http({
      url: http.adornUrl('/admin/area/list'),
      method: 'get',
      params: http.adornParams()
    })
      .then(({ data }) => {
        areaList.value = treeDataTranslate(data, 'areaId', 'parentId')
      })
  })
}
defineExpose({ init })

const page = {
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 20 // 每页显示多少条
}
/**
 * 表单提交
 */
const onSubmit = Debounce(() => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      http({
        url: http.adornUrl('/admin/area'),
        method: dataForm.value.areaId ? 'put' : 'post',
        data: http.adornData(dataForm.value)
      }).then(() => {
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

const handleChange = (val) => {
  dataForm.value.parentId = val[val.length - 1]
}

</script>
