<template>
  <div class="mod-hotSearch-add-or-update">
    <el-dialog :title="!dataForm.id ? '新增' : '修改'"
               :close-on-click-modal="false"
               :visible.sync="visible">
      <el-form :model="dataForm"
               :rules="dataRule"
               ref="dataForm"
               @keyup.enter.native="dataFormSubmit()"
               label-width="80px">

        <el-form-item label="标题"
                      prop="title">
          <el-input v-model="dataForm.title"
                    controls-position="right"
                    :min="0"
                    label="标题"></el-input>
        </el-form-item>

        <el-form-item label="内容"
                      prop="content">
          <el-input v-model="dataForm.content"
                    controls-position="right"
                    :min="0"
                    label="内容"></el-input>
        </el-form-item>
        <el-form-item label="排序号"
                      prop="seq">
          <el-input-number v-model="dataForm.seq"
                           controls-position="right"
                           :min="0"
                           label="排序号"></el-input-number>
        </el-form-item>
        <el-form-item label="状态"
                      size="mini"
                      prop="status">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="0">下线</el-radio>
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
  </div>
</template>

<script>
export default {
  data () {
    return {
      dataForm: {
        hotSearchId: 0,
        title: '',
        content: '',
        recDate: '',
        seq: 0,
        status: 0
      },
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      },
      addProdVisible: false,
      visible: false,
      resourcesUrl: window.SITE_CONFIG.resourcesUrl,
      dataRule: {
        title: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在1到50个字符内', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { min: 1, max: 255, message: '长度在1到255个字符内', trigger: 'blur' }
        ]
      }
    }
  },
  components: {},
  methods: {
    init (data) {
      this.visible = true
      if (data) {
        this.dataForm = Object.assign({}, data)
      } else {
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
        })
      }
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          let param = this.dataForm
          this.$http({
            url: this.$http.adornUrl(`/admin/hotSearch`),
            method: param.hotSearchId ? 'put' : 'post',
            data: this.$http.adornData(param)
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
    }
  }
}
</script>
