<template>
  <el-dialog :title="!dataForm.areaId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="地区名称"
                    prop="areaName">
        <el-input v-model="dataForm.areaName"></el-input>
      </el-form-item>
      <el-form-item label="上级地区"
                    prop="parentId">
        <el-cascader expand-trigger="hover"
                     :options="areaList"
                     :props="categoryTreeProps"
                     change-on-select
                     filterable
                     v-model="selectedOptions"
                     @change="handleChange">
        </el-cascader>
      </el-form-item>
    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button @click="visible = false"
                 size="mini">取消</el-button>
      <el-button type="primary"
                 @click="dataFormSubmit()"
                 size="mini">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { treeDataTranslate } from '@/utils'
export default {
  data () {
    return {
      visible: false,
      roleList: [],
      dataForm: {
        areaId: '',
        areaName: null,
        parentId: null,
        level: null
      },
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      dataRule: {
        areaName: [
          { required: true, message: '区域名称不能为空', trigger: 'blur' }
        ]
      },
      areaList: [],
      categoryTreeProps: {
        value: 'areaId',
        label: 'areaName'
      },
      selectedOptions: []
    }
  },
  methods: {
    init (areaId) {
      this.dataForm.areaId = areaId || 0
      this.visible = true
      this.selectedOptions = []
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.areaId) {
          this.$http({
            url: this.$http.adornUrl('/admin/area/info/' + this.dataForm.areaId),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            this.dataForm = data
            this.selectedOptions.push(this.dataForm.parentId)
            this.categoryTreeProps.areaId = this.dataForm.areaId
            this.categoryTreeProps.areaName = this.dataForm.areaName
          })
        }

        this.$http({
          url: this.$http.adornUrl('/admin/area/list'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.areaList = treeDataTranslate(data, 'areaId', 'parentId')
        })
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/admin/area'),
            method: this.dataForm.areaId ? 'put' : 'post',
            data: this.$http.adornData(this.dataForm)
          }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.visible = false
                this.$emit('refreshDataList', this.page)
              }
            })
          })
        }
      })
    },
    handleChange (val) {
      this.dataForm.parentId = val[val.length - 1]
    }
  }
}
</script>