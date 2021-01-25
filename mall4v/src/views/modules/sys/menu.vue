<template>
  <div class="mod-menu">
    <el-form :inline="true"
             :model="dataForm">
      <el-form-item>
        <el-button v-if="isAuth('sys:menu:save')"
                   type="primary"
                   @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList"
              border
              style="width: 100%;"
              row-key="menuId">
      <el-table-column prop="name"
                       header-align="center"
                       treeKey="menuId"
                       width="150"
                       label="名称">
      </el-table-column>
      <el-table-column header-align="center"
                       align="center"
                       label="图标">
        <template slot-scope="scope">
          <icon-svg :name="scope.row.icon || ''"></icon-svg>
        </template>
      </el-table-column>
      <el-table-column prop="type"
                       header-align="center"
                       align="center"
                       label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0"
                  size="small">目录</el-tag>
          <el-tag v-else-if="scope.row.type === 1"
                  size="small"
                  type="success">菜单</el-tag>
          <el-tag v-else-if="scope.row.type === 2"
                  size="small"
                  type="info">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum"
                       header-align="center"
                       align="center"
                       label="排序号">
      </el-table-column>
      <el-table-column prop="url"
                       header-align="center"
                       align="center"
                       width="150"
                       :show-overflow-tooltip="true"
                       label="菜单URL">
      </el-table-column>
      <el-table-column prop="perms"
                       header-align="center"
                       align="center"
                       width="150"
                       :show-overflow-tooltip="true"
                       label="授权标识">
      </el-table-column>
      <el-table-column fixed="right"
                       header-align="center"
                       align="center"
                       width="150"
                       label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('sys:menu:update')"
                     type="text"
                     size="small"
                     @click="addOrUpdateHandle(scope.row.menuId)">修改</el-button>
          <el-button v-if="isAuth('sys:menu:delete')"
                     type="text"
                     size="small"
                     @click="deleteHandle(scope.row.menuId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible"
                   ref="addOrUpdate"
                   @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './menu-add-or-update'
import { treeDataTranslate } from '@/utils'
export default {
  data () {
    return {
      dataForm: {},
      dataList: [],
      dataListLoading: false,
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/sys/menu/table'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        this.dataList = treeDataTranslate(data, 'menuId')
        this.dataListLoading = false
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
    deleteHandle (id) {
      this.$confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/sys/menu/${id}`),
          method: 'delete',
          data: this.$http.adornData()
        }).then(({ data }) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getDataList()
            }
          })
        })
      })
    }
  }
}
</script>
