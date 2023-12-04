<template>
  <div class="mod-prod">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :option="tableOption"
      :permission="permission"
      @search-change="onSearch"
      @on-load="getDataList"
    >
      <template #prodPropValues="scope">
        <el-tag
          v-for="item in scope.row.prodPropValues"
          :key="item.valueId"
        >
          {{ item.propValue }}
        </el-tag>
      </template>
      <template #menu-left>
        <el-button
          v-if="isAuth('shop:pickAddr:save')"
          type="primary"
          icon="el-icon-plus"

          @click.stop="onAddOrUpdate()"
        >
          新增
        </el-button>
      </template>
      <template #menu="scope">
        <el-button
          v-if="isAuth('prod:spec:update')"
          type="primary"
          icon="el-icon-edit"

          @click.stop="onAddOrUpdate(scope.row)"
        >
          编辑
        </el-button>

        <el-button
          v-if="isAuth('prod:spec:delete')"
          type="danger"
          icon="el-icon-delete"

          @click.stop="onDelete(scope.row.propId)"
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
import { tableOption } from '@/crud/prod/spec.js'
const permission = ref({
  delBtn: isAuth('prod:prod:delete')
})
const dataList = ref([])
const dataListLoading = ref(false)
const dataListSelections = ref([])
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
/**
 * 获取数据列表
 */
const getDataList = (pageParam, params, done) => {
  dataListLoading.value = true
  http({
    url: http.adornUrl('/prod/spec/page'),
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
 * @param val
 */
const onAddOrUpdate = (val) => {
  addOrUpdateVisible.value = true
  nextTick(() => {
    addOrUpdateRef.value?.init(val)
  })
}

/**
 * 删除
 * @param id
 */
const onDelete = (id) => {
  const ids = id ? [id] : dataListSelections.value?.map(item => {
    return item.propId
  })
  ElMessageBox.confirm(`确定进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      http({
        url: http.adornUrl(`/prod/spec/${ids}`),
        method: 'delete',
        data: http.adornData(ids, false)
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
    })
    .catch(() => { })
}

const onSearch = (params, done) => {
  getDataList(page, params, done)
}

</script>

<style scoped lang="scss">
:deep(.prop-value) {
  display: inline-block;
  margin: 0 3px 3px 0;
}
</style>
