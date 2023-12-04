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
        label="角色名称"
        prop="roleName"
      >
        <el-input
          v-model="dataForm.roleName"
          placeholder="角色名称"
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
      <el-form-item label="授权">
        <el-tree
          ref="menuListTreeRef"
          :data="menuList"
          :props="menuListTreeProps"
          node-key="menuId"
          show-checkbox
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
import { treeDataTranslate } from '@/utils'
import { Debounce } from '@/utils/debounce'

const emit = defineEmits(['refreshDataList'])
const tempKey = -666666 // 临时key) 用于解决tree半选中状态项不能传给后台接口问题. # 待优化
const visible = ref(false)
const menuList = ref([])
const menuListTreeProps = {
  label: 'name',
  children: 'children'
}
const dataForm = reactive({
  id: 0,
  roleName: '',
  remark: ''
})
const dataRule = reactive({
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' },
    { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的角色名称', trigger: 'blur' }
  ],
  remark: [
    { required: false, pattern: /\s\S+|S+\s|\S/, message: '输入格式有误', trigger: 'blur' }
  ]
})
const dataFormRef = ref(null)
const menuListTreeRef = ref(null)
const init = (id) => {
  dataForm.id = id || 0
  http({
    url: http.adornUrl('/sys/menu/table'),
    method: 'get',
    params: http.adornParams()
  })
    .then(({ data }) => {
      menuList.value = treeDataTranslate(data, 'menuId', 'parentId')
    })
    .then(() => {
      visible.value = true
      nextTick(() => {
        dataFormRef.value?.resetFields()
        menuListTreeRef.value?.setCheckedKeys([])
      })
    })
    .then(() => {
      if (dataForm.id) {
        http({
          url: http.adornUrl(`/sys/role/info/${dataForm.id}`),
          method: 'get',
          params: http.adornParams()
        })
          .then(({ data }) => {
            dataForm.roleName = data.roleName
            dataForm.remark = data.remark
            const idx = data.menuIdList.indexOf(tempKey)
            if (idx !== -1) {
              data.menuIdList.splice(idx, data.menuIdList.length - idx)
            }
            menuListTreeRef.value?.setCheckedKeys(data.menuIdList)
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
        url: http.adornUrl('/sys/role'),
        method: dataForm.id ? 'put' : 'post',
        data: http.adornData({
          roleId: dataForm.id || undefined,
          roleName: dataForm.roleName,
          remark: dataForm.remark,
          menuIdList: [].concat(menuListTreeRef.value?.getCheckedKeys(), [tempKey], menuListTreeRef.value?.getHalfCheckedKeys())
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
})

</script>
