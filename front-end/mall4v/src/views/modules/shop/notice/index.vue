<template>
  <div class="mod-shop-notice">
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
      <template #status="scope">
        <el-tag
          v-if="scope.row.status === 0"
          type="danger"
        >
          撤销
        </el-tag>
        <el-tag v-else>
          公布
        </el-tag>
      </template>
      <template #isTop="scope">
        <el-tag v-if="scope.row.isTop === 0">
          否
        </el-tag>
        <el-tag v-else>
          是
        </el-tag>
      </template>
      <template #menu-left>
        <el-button
          v-if="isAuth('shop:notice:save')"
          type="primary"
          icon="el-icon-plus"
          @click="onAddOrUpdate()"
        >
          新增
        </el-button>
      </template>
      <template #menu="scope">
        <el-button
          v-if="isAuth('shop:notice:update')"
          type="primary"
          icon="el-icon-edit"
          @click="onAddOrUpdate(scope.row.id)"
        >
          修改
        </el-button>
        <el-button
          v-if="isAuth('shop:notice:delete')"
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
      @close="addOrUpdateVisible=false"
    />
  </div>
</template>

<script setup>
import { isAuth } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'
import { tableOption } from '@/crud/shop/notice'
import AddOrUpdate from './add-or-update.vue'

const dataList = ref([])
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
const dataListLoading = ref(false)
const addOrUpdateVisible = ref(false)

const getDataList = (pageParam, params, done) => {
  dataListLoading.value = true
  http({
    url: http.adornUrl('/shop/notice/page'),
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
        url: http.adornUrl('/shop/notice/' + id),
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

<style lang="scss" scoped>
</style>
