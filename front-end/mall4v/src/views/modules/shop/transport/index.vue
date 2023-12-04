<template>
  <div class="mod-transport">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :option="tableOption"
      @search-change="onSearch"
      @selection-change="selectionChange"
      @on-load="getDataList"
    >
      <template #prod-prop-values="scope">
        <el-tag
          v-for="item in scope.row.prodPropValues"
          :key="item.valueId"
        >
          {{ item.propValue }}
        </el-tag>
      </template>
      <template #menu-left>
        <el-button
          v-if="isAuth('shop:transport:save')"
          type="primary"
          icon="el-icon-plus"
          @click.stop="onAddOrUpdate()"
        >
          新增
        </el-button>

        <el-button
          v-if="isAuth('shop:transport:delete')"
          type="danger"
          :disabled="dataListSelections.length <= 0"
          @click="onDelete()"
        >
          批量删除
        </el-button>
      </template>

      <template #menu="scope">
        <el-button
          v-if="isAuth('shop:transport:update')"
          type="primary"
          icon="el-icon-edit"
          @click.stop="onAddOrUpdate(scope.row.transportId)"
        >
          修改
        </el-button>

        <el-button
          v-if="isAuth('shop:transport:delete')"
          type="danger"
          icon="el-icon-delete"
          @click.stop="onDelete(scope.row.transportId)"
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
import { tableOption } from '@/crud/shop/transport'
import AddOrUpdate from './add-or-update.vue'
const dataList = ref([])
const dataListSelections = ref([])
const page = ref({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})

/**
 * 获取数据列表
 */
const getDataList = (pageParam, params, done) => {
  http({
    url: http.adornUrl('/shop/transport/page'),
    method: 'get',
    params: http.adornParams(
      Object.assign(
        {
          current: pageParam == null ? page.value.currentPage : pageParam.currentPage,
          size: pageParam == null ? page.value.pageSize : pageParam.pageSize
        },
        params
      )
    )
  })
    .then(({ data }) => {
      dataList.value = data.records
      page.value.total = data.total
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
  const ids = id ? [id] : dataListSelections.value?.map(item => item.transportId)
  ElMessageBox.confirm(
    `确定进行[${id ? '删除' : '批量删除'}]操作?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(() => {
      http({
        url: http.adornUrl('/shop/transport'),
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
    }).catch(() => {})
}

/**
 * 条件查询
 */
const onSearch = (params, done) => {
  getDataList(page.value, params, done)
}

/**
 * 多选变化
 */
const selectionChange = (val) => {
  dataListSelections.value = val
}

</script>
