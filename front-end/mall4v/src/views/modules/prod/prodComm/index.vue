<template>
  <div class="mod-prod-prodComm">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :table-loading="dataListLoading"
      :option="tableOption"
      @search-change="onSearch"
      @on-load="getDataList"
      @refresh-change="refreshChange"
      @row-del="rowDel"
    >
      <template #nickName="scope">
        {{ scope.row.user.nickName }}
      </template>
      <template #replyTime="scope">
        {{ scope.row.replyTime ? scope.row.replyTime : '-' }}
      </template>

      <template #menu="scope">
        <el-button
          type="primary"
          icon="el-icon-edit"
          @click="onAddOrUpdate(scope.row.prodCommId,true)"
        >
          编辑
        </el-button>

        <el-button
          type="success"
          icon="el-icon-view"
          @click="onAddOrUpdate(scope.row.prodCommId,false)"
        >
          查看
        </el-button>
      </template>
    </avue-crud>
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdateRef"
      @refresh-data-list="refreshChange"
    />
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { tableOption } from '@/crud/prod/prodComm.js'
import AddOrUpdate from './add-or-update.vue'

const dataList = ref([])
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 20 // 每页显示多少条
})
const dataListLoading = ref(false)

const getDataList = (pageParam, params, done) => {
  dataListLoading.value = true
  http({
    url: http.adornUrl('/prod/prodComm/page'),
    method: 'get',
    params: http.adornParams(Object.assign({
      current: pageParam == null ? page.currentPage : pageParam.currentPage,
      size: pageParam == null ? page.pageSize : pageParam.pageSize
    }, params))
  })
    .then(({ data }) => {
      dataList.value = data.records
      page.total = data.total
      dataListLoading.value = false
      if (done) done()
    })
}

const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref(null)
/**
 * 新增 / 修改
 */
const onAddOrUpdate = (id, isEdit) => {
  addOrUpdateVisible.value = true
  nextTick(() => {
    addOrUpdateRef.value?.init(id, isEdit)
  })
}
const rowDel = (row) => {
  ElMessageBox.confirm('确定进行删除操作?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      http({
        url: http.adornUrl('/prod/prodComm/' + row.prodCommId),
        method: 'delete',
        data: http.adornData({})
      })
        .then(() => {
          ElMessage({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              getDataList()
            }
          })
        })
    }).catch(() => { })
}
/**
 * 刷新回调
 */
const refreshChange = () => {
  getDataList(page)
}
const onSearch = (params, done) => {
  getDataList(page, params, done)
}
</script>
