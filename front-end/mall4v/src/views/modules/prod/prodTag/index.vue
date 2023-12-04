<template>
  <div class="mod-prod-prodTag">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :table-loading="dataListLoading"
      :option="tableOption"
      @search-change="onSearch"
      @on-load="getDataList"
      @refresh-change="refreshChange"
    >
      <template #menu-left>
        <el-button
          v-if="isAuth('prod:prodTag:save')"
          type="primary"
          icon="el-icon-plus"
          @click="onAddOrUpdate()"
        >
          新增
        </el-button>
      </template>
      <template #title="scope">
        {{ scope.row.title || '-' }}
      </template>
      <template #status="scope">
        <el-tag
          v-if="scope.row.status === 0"
          type="danger"
        >
          禁用
        </el-tag>
        <el-tag v-else>
          正常
        </el-tag>
      </template>

      <template #isDfault="scope">
        <el-tag v-if="scope.row.isDefault === 0">
          自定义类型
        </el-tag>
        <el-tag v-else>
          默认类型
        </el-tag>
      </template>

      <template #menu="scope">
        <el-button
          v-if="isAuth('prod:prodTag:update')"
          type="primary"
          icon="el-icon-edit"
          @click="onAddOrUpdate(scope.row.id)"
        >
          修改
        </el-button>
        <el-button
          v-if="isAuth('prod:prodTag:delete')"
          type="danger"
          icon="el-icon-delete"
          @click.stop="onDelete(scope.row.id)"
        >
          删除
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
import { isAuth } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'
import { tableOption } from '@/crud/prod/prodTag.js'
import AddOrUpdate from './add-or-update.vue'

const dataList = ref([])
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
const dataListLoading = ref(false)

const getDataList = (pageParam, params, done) => {
  dataListLoading.value = true
  http({
    url: http.adornUrl('/prod/prodTag/page'),
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
 * @param id
 */
const onAddOrUpdate = (id) => {
  addOrUpdateVisible.value = true
  nextTick(() => {
    addOrUpdateRef.value?.init(id)
  })
}

const onDelete = (id) => {
  ElMessageBox.confirm('确定进行删除操作?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      http({
        url: http.adornUrl('/prod/prodTag/' + id),
        method: 'delete',
        data: http.adornData({})
      })
        .then(() => {
          ElMessage({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              getDataList(page)
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

<style lang="scss" scoped>
</style>
