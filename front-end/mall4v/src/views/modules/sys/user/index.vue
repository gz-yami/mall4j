<template>
  <div class="mod-user">
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
          v-if="isAuth('sys:user:save')"
          type="primary"
          icon="el-icon-plus"
          @click.stop="onAddOrUpdate()"
        >
          新增
        </el-button>

        <el-button
          v-if="isAuth('sys:user:delete')"
          type="danger"

          :disabled="dataListSelections.length <= 0"
          @click="onDelete()"
        >
          批量删除
        </el-button>
      </template>
      <template
        #menu="scope"
      >
        <el-button
          v-if="isAuth('sys:user:update')"
          type="primary"
          icon="el-icon-edit"
          @click.stop="onAddOrUpdate(scope.row.userId)"
        >
          编辑
        </el-button>

        <el-button
          v-if="isAuth('sys:user:delete')"
          type="danger"
          icon="el-icon-delete"
          @click.stop="onDelete(scope.row.userId)"
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
import { tableOption } from '@/crud/sys/user.js'
import AddOrUpdate from './add-or-update.vue'

const dataList = ref([])
const dataListLoading = ref(false)
const dataListSelections = ref([])
const addOrUpdateVisible = ref(false)
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
    url: http.adornUrl('/sys/user/page'),
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
  }).then(({ data }) => {
    dataList.value = data.records
    page.total = data.total
    dataListLoading.value = false
    if (done) done()
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

const addOrUpdateRef = ref(null)
/**
 * 新增 / 修改
 */
const onAddOrUpdate = (id) => {
  addOrUpdateVisible.value = true
  nextTick(() => {
    addOrUpdateRef.value?.init(id)
  })
}
/**
 * 删除
 */
const onDelete = (id) => {
  const userIds = id ? [id] : dataListSelections.value?.map(item => {
    return item.userId
  })
  ElMessageBox.confirm(`确定对[id=${userIds.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http({
      url: http.adornUrl('/sys/user'),
      method: 'delete',
      data: http.adornData(userIds, false)
    }).then(() => {
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
