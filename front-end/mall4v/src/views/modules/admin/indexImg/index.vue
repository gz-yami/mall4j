<template>
  <div class="mod-prod">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :table-loading="dataListLoading"
      :option="tableOption"
      @search-change="onSearch"
      @selection-change="selectionChange"
      @on-load="getDataList"
    >
      <template #menu-left>
        <el-button
          v-if="isAuth('admin:indexImg:save')"
          type="primary"
          icon="el-icon-plus"
          @click.stop="onAddOrUpdate()"
        >
          新增
        </el-button>

        <el-button
          v-if="isAuth('admin:indexImg:delete')"
          type="danger"
          :disabled="dataListSelections.length <= 0"
          @click="onDelete()"
        >
          批量删除
        </el-button>
      </template>

      <template #imgUrl="scope">
        <img
          v-if="scope.row.imgUrl"
          alt=""
          :src="scope.row.imgUrl"
          width="100"
          height="100"
        >
        <img
          v-else
          alt=""
          src="~@/assets/img/def.png"
          width="100"
          height="100"
        >
      </template>
      <template #menu="scope">
        <el-button
          v-if="isAuth('admin:indexImg:update')"
          type="primary"
          icon="el-icon-edit"
          @click="onAddOrUpdate(scope.row.imgId)"
        >
          修改
        </el-button>
        <el-button
          v-if="isAuth('admin:indexImg:delete')"
          type="danger"
          icon="el-icon-delete"
          @click="onDelete(scope.row.imgId)"
        >
          删除
        </el-button>
      </template>
    </avue-crud>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdateRef"
      @refresh-data-list="getDataList"
    />
  </div>
</template>

<script setup>
import { isAuth } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'
import AddOrUpdate from './add-or-update.vue'
import { tableOption } from '@/crud/admin/indexImg.js'

const dataList = ref([])
const dataListLoading = ref(false)
const dataListSelections = ref([])
const resourcesUrl = import.meta.env.VITE_APP_RESOURCES_URL
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少
})
/**
 * 获取数据列表
 */
const getDataList = (pageParam, params, done) => {
  dataListLoading.value = true
  http({
    url: http.adornUrl('/admin/indexImg/page'),
    method: 'get',
    params: http.adornParams(
      Object.assign(
        {
          current: pageParam == null ? page.currentPage : pageParam.currentPage,
          size: pageParam == null ? page.pageSize : pageParam.pageSize
        },
        params
      )
    )
  })
    .then(({ data }) => {
      data.records.forEach(item => {
        item.imgUrl = item.imgUrl ? resourcesUrl + item.imgUrl : ''
      })
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
/**
 * 删除
 * @param id
 */
const onDelete = (id) => {
  const ids = id ? [id] : dataListSelections.value?.map(item => {
    return item.imgId
  })
  ElMessageBox.confirm(`确定进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      http({
        url: http.adornUrl('/admin/indexImg'),
        method: 'delete',
        data: http.adornData(ids, false)
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
    })
}
/**
 * 条件查询
 */
const onSearch = (params, done) => {
  getDataList(page, params, done)
}
/**
 * 多选变化
 */
const selectionChange = (val) => {
  dataListSelections.value = val
}
</script>
