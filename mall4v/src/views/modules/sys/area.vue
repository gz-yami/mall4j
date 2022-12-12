<template>
  <div class="mod-sys-area">
    <el-input v-model="areaName"
              class="area-search-input"
              placeholder="地区关键词">
    </el-input>
    <el-button type="primary"
               size="mini"
               class="area-add-btn"
               @click="addOrUpdateHandle()">新增</el-button>

    <el-tree :data="data"
             node-key="areaId"
             :filter-node-method="filterNode"
             ref="tree2"
             :props="props"
             :expand-on-click-node="false">

      <span class="custom-tree-node"
            slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button type="text"
                     icon="el-icon-edit"
                     size="small"
                     @click="() => update(node, data)">
            修改
          </el-button>
          <el-button type="text"
                     icon="el-icon-delete"
                     size="small"
                     @click="() => remove(node, data)">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>

    <add-or-update v-if="addOrUpdateVisible"
                   ref="addOrUpdate"
                   @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import { tableOption } from '@/crud/sys/area'
import AddOrUpdate from './area-add-or-update'
import { treeDataTranslate } from '@/utils'
export default {
  data () {
    return {
      dataList: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      },
      dataListLoading: false,
      tableOption: tableOption,
      addOrUpdateVisible: false,
      areaName: '',
      dataForm: {},
      data: [],
      params: {
        areaName: null
      },
      props: {
        id: 'areaId',
        label: 'areaName',
        children: 'children'
      }
    }
  },
  created () {
    this.getDataList(this.page)
  },
  mounted () {
  },
  components: {
    AddOrUpdate
  },
  watch: {
    areaName (val) {
      this.$refs.tree2.filter(val)
    }
  },
  methods: {
    getDataList (page, params, done) {
      this.$http({
        url: this.$http.adornUrl('/admin/area/list'),
        method: 'get',
        params: this.$http.adornParams(Object.assign({
          current: page == null ? this.page.currentPage : page.currentPage,
          size: page == null ? this.page.pageSize : page.pageSize
        }, params))
      }).then(({ data }) => {
        let treeData = treeDataTranslate(data, 'areaId', 'parentId')
        this.data = treeData
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },

    // 删除
    deleteHandle (areaId) {
      this.$confirm('确定进行删除操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/admin/area/' + areaId),
          method: 'delete',
          data: this.$http.adornData({})
        }).then(({ data }) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getDataList(this.page)
            }
          })
        })
      }).catch(() => { })
    },
    update (node, data) {
      this.addOrUpdateHandle(data.areaId)
    },

    remove (node, data) {
      this.deleteHandle(data.areaId)
    },

    filterNode (value, data) {
      if (!value) return true
      return data.areaName.indexOf(value) !== -1
    }
  }

}
</script>

<style lang="scss" scoped>
.mod-sys-area {
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .area-search-input {
    width: 30%;
  }
  .area-add-btn {
    float: right;
  }
}
</style>
