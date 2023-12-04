<template>
  <div class="mod-config">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :option="tableOption"
      @search-change="onSearch"
      @selection-change="selectionChange"
      @on-load="getDataList"
    >
      <template #menu-left>
        <el-button
          type="primary"
          icon="el-icon-plus"
          @click.stop="onAddOrUpdate()"
        >
          新增
        </el-button>

        <el-button
          type="danger"
          :disabled="dataListSelections.length <= 0"
          @click="onDelete()"
        >
          批量删除
        </el-button>
      </template>
      <template #menu="scope">
        <el-button
          type="primary"
          icon="el-icon-edit"
          @click.stop="onAddOrUpdate(scope.row.id)"
        >
          编辑
        </el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click.stop="onDelete(scope.row.id)"
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { tableOption } from '@/crud/sys/config.js'
const dataList = ref([])
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
/**
 * 获取数据列表
 */
const getDataList = (pageParam, params, done) => {
  http({
    url: http.adornUrl('/sys/config/page'),
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
      if (done) done()
    })
}
/**
 * 条件查询
 */
const onSearch = (params, done) => {
  getDataList(page, params, done)
}

const dataListSelections = ref([])
/**
 * 多选变化
 */
const selectionChange = (val) => {
  dataListSelections.value = val
}

const addOrUpdateVisible = ref(false)
const addOrUpdate = ref(null)
/**
 * 新增 / 修改
 */
const onAddOrUpdate = (id) => {
  addOrUpdateVisible.value = true
  nextTick(() => {
    addOrUpdate.value?.init(id)
  })
}
/**
 * 删除
 */
const onDelete = (id) => {
  const ids = id ? [id] : dataListSelections.value?.map(item => {
    return item.id
  })
  ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      http({
        url: http.adornUrl('/sys/config'),
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
    }).catch(() => { })
}
</script>
