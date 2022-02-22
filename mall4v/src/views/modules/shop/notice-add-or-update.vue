<template>
  <el-dialog :title="!dataForm.id ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="公告标题"
                    prop="title">
        <el-input v-model="dataForm.title"></el-input>
      </el-form-item>
      <el-form-item label="状态"
                    prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">公布</el-radio>
          <el-radio :label="0">撤销</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="置顶"
                    prop="isTop">
        <el-radio-group v-model="dataForm.isTop">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="公告内容"
                    prop="content">
        <tiny-mce v-model="dataForm.content"></tiny-mce>
      </el-form-item>

    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button size="small"
                 @click="visible = false">取消</el-button>
      <el-button size="small"
                 type="primary"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import TinyMce from '@/components/tiny-mce'
import { Debounce } from '@/utils/debounce'
export default {
  data () {
    var validateTitle = (rule, value, callback) => {
      if (!value.trim()) {
        this.dataForm.title = ''
        callback(new Error('公告标题不能为空'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      roleList: [],
      dataForm: {
        title: null,
        content: null,
        url: null,
        status: 1,
        isTop: 0
      },
      dataRule: {
        title: [
          {required: true, message: '公告标题不能为空', trigger: 'blur'},
          { validator: validateTitle, trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    TinyMce
  },
  methods: {
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl('/shop/notice/info/' + this.dataForm.id),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            this.dataForm = data
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit: Debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/shop/notice'),
            method: this.dataForm.id ? 'put' : 'post',
            data: this.$http.adornData(this.dataForm)
          }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.visible = false
                this.$emit('refreshDataList')
                this.dataForm.content = ''
              }
            })
          })
        }
      })
    })
  }
}
</script>
