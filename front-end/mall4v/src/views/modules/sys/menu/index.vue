<template>
  <div class="mod-menu">
    <el-form
      :inline="true"
      :model="dataForm"
    >
      <el-form-item>
        <el-button
          v-if="isAuth('sys:menu:save')"
          type="primary"
          @click="onAddOrUpdate()"
        >
          新增
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      style="width: 100%;"
      row-key="menuId"
    >
      <el-table-column
        prop="name"
        header-align="center"
        tree-key="menuId"
        width="150"
        label="名称"
      />
      <el-table-column
        header-align="center"
        align="center"
        label="图标"
      >
        <template #default="scope">
          <svg-icon
            :icon-class="`icon-${scope.row.icon}`"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="类型"
      >
        <template #default="scope">
          <el-tag
            v-if="scope.row.type === 0"
          >
            目录
          </el-tag>
          <el-tag
            v-else-if="scope.row.type === 1"

            type="success"
          >
            菜单
          </el-tag>
          <el-tag
            v-else-if="scope.row.type === 2"

            type="info"
          >
            按钮
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="orderNum"
        header-align="center"
        align="center"
        label="排序号"
      />
      <el-table-column
        prop="url"
        header-align="center"
        align="center"
        width="150"
        :show-overflow-tooltip="true"
        label="菜单URL"
      >
        <template #default="scope">
          {{ scope.row.url || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="perms"
        header-align="center"
        align="center"
        width="150"
        :show-overflow-tooltip="true"
        label="授权标识"
      >
        <template #default="scope">
          {{ scope.row.perms || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作"
      >
        <template #default="scope">
          <el-button
            v-if="isAuth('sys:menu:update')"
            type="text"

            @click="onAddOrUpdate(scope.row.menuId)"
          >
            修改
          </el-button>
          <el-button
            v-if="isAuth('sys:menu:delete')"
            type="text"

            @click="onDelete(scope.row.menuId)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdateRef"
      @refresh-data-list="getDataList"
      @close="addOrUpdateVisible=false"
    />
  </div>
</template>

<script setup>
import { treeDataTranslate, isAuth } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'
import AddOrUpdate from './add-or-update.vue'

const dataForm = ref({})
onMounted(() => {
  getDataList()
})

const dataList = ref([])
/**
 * 获取数据列表
 */
const getDataList = () => {
  http({
    url: http.adornUrl('/sys/menu/table'),
    method: 'get',
    params: http.adornParams()
  }).then(({ data }) => {
    dataList.value = treeDataTranslate(data, 'menuId')
  })
}
const addOrUpdateRef = ref(null)
const addOrUpdateVisible = ref(false)
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
  ElMessageBox.confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http({
      url: http.adornUrl(`/sys/menu/${id}`),
      method: 'delete',
      data: http.adornData()
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
  })
}
</script>
