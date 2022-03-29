<template>
  <div class="mod-hotSearch-add-or-update">
    <el-dialog :title="!dataForm.hotSearchId ? '新增' : '修改'"
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
                    maxlength="50"
                    show-word-limit
                    label="标题"></el-input>
        </el-form-item>

        <el-form-item label="内容"
                      prop="content">
          <el-input v-model="dataForm.content"
                    controls-position="right"
                    type="textarea"
                    :min="0"
                    maxlength="255"
                    show-word-limit
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
import { Debounce } from '@/utils/debounce'
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
      resourcesUrl: process.env.VUE_APP_RESOURCES_URL,
      dataRule: {
        title: [
          { required: true, message: '标题不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在1到50个字符内', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '标题不能为空', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '内容不能为空', trigger: 'blur' },
          { min: 1, max: 255, message: '长度在1到255个字符内', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '内容不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  components: {},
  methods: {
    init (id) {
      this.dataForm.hotSearchId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.hotSearchId) {
          this.$http({
            url: this.$http.adornUrl('/admin/hotSearch/info/' + this.dataForm.hotSearchId),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            this.dataForm = data
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit: Debounce(function () {
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
    })
  }
}
</script>
