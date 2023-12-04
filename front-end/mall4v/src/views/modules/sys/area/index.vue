<template>
  <div class="mod-sys-area">
    <el-input
      v-model="areaName"
      class="area-search-input"
      placeholder="地区关键词"
    />
    <el-button
      type="primary"
      class="area-add-btn"
      @click="onAddOrUpdate()"
    >
      新增
    </el-button>

    <el-tree
      ref="tree2Ref"
      :data="areaTreeData"
      node-key="areaId"
      :filter-node-method="filterNode"
      :props="props"
      :expand-on-click-node="false"
    >
      <template #default="{ node, data }">
        <span class="addr-name">{{ node.label }}</span>
        <span>
          <el-button
            type="text"
            icon="el-icon-edit"
            @click="() => update(node, data)"
          >
            修改
          </el-button>
          <el-button
            type="text"
            icon="el-icon-delete"
            @click="() => remove(node, data)"
          >
            删除
          </el-button>
        </span>
      </template>
    </el-tree>

    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdateRef"
      @refresh-data-list="getDataList"
      @close="addOrUpdateVisible=false"
    />
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import AddOrUpdate from './add-or-update.vue'
import { treeDataTranslate } from '@/utils'

const areaName = ref('')
const props = {
  id: 'areaId',
  label: 'areaName',
  children: 'children'
}

const tree2Ref = ref(null)
watch(
  () => areaName,
  (val) => {
    tree2Ref.value?.filter(val)
  }
)

onMounted(() => {
  getDataList(page)
})

const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
const areaTreeData = ref(null)
const getDataList = (pageParam, params) => {
  http({
    url: http.adornUrl('/admin/area/list'),
    method: 'get',
    params: http.adornParams(Object.assign({
      current: pageParam == null ? page.currentPage : pageParam.currentPage,
      size: pageParam == null ? page.pageSize : pageParam.pageSize
    }, params))
  }).then(({ data }) => {
    areaTreeData.value = treeDataTranslate(data, 'areaId', 'parentId')
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
 * @param areaId
 */
const onDelete = (areaId) => {
  ElMessageBox.confirm('确定进行删除操作?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http({
      url: http.adornUrl('/admin/area/' + areaId),
      method: 'delete',
      data: http.adornData({})
    }).then(() => {
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
// eslint-disable-next-line no-unused-vars
const update = (node, data) => {
  onAddOrUpdate(data.areaId)
}

// eslint-disable-next-line no-unused-vars
const remove = (node, data) => {
  onDelete(data.areaId)
}

const filterNode = (value, data) => {
  if (!value) return true
  return data.areaName.indexOf(value) !== -1
}

</script>

<style lang="scss" scoped>
.mod-sys-area {
  .area-search-input {
    width: 30%;
  }
  .area-add-btn {
    float: right;
  }
}
:deep(.el-tree-node) .addr-name {
  width: 100%;
}
</style>
