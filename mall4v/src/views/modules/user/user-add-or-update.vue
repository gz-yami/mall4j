<template>
  <el-dialog :title="!dataForm.userId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="用户头像"
                    prop="pic">
        <img :src="dataForm.pic"
             class="image">
      </el-form-item>
      <el-form-item label="用户昵称"
                    prop="nickName">
        <el-input v-model="dataForm.nickName"
                  :disabled="true"
                  placeholder="用户昵称"></el-input>
      </el-form-item>
      <el-form-item label="状态"
                    size="mini"
                    prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { Debounce } from '@/utils/debounce'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        userId: 0,
        nickName: '',
        pic: '',
        status: 1
      },
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      },
      resourcesUrl: process.env.VUE_APP_RESOURCES_URL,
      dataRule: {
        nickName: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.userId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields()
      })
      if (this.dataForm.userId) {
        this.$http({
          url: this.$http.adornUrl(`/admin/user/info/${this.dataForm.userId}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.dataForm = data
        })
      }
    },
    // 表单提交
    dataFormSubmit: Debounce(function () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/admin/user`),
            method: this.dataForm.userId ? 'put' : 'post',
            data: this.$http.adornData({
              userId: this.dataForm.userId || undefined,
              nickName: this.dataForm.nickName,
              status: this.dataForm.status
            })
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
    })
  }
}
</script>
