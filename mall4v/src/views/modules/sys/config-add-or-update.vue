<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="参数名" prop="paramKey">
        <el-input v-model="dataForm.paramKey" placeholder="参数名"></el-input>
      </el-form-item>
      <el-form-item label="参数值" prop="paramValue">
        <el-input v-model="dataForm.paramValue" placeholder="参数值"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
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
        id: 0,
        paramKey: '',
        paramValue: '',
        remark: ''
      },
      dataRule: {
        paramKey: [
            { required: true, message: '参数名不能为空', trigger: 'blur' },
            { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的参数名', trigger: 'blur' }
        ],
        paramValue: [
            { required: true, message: '参数值不能为空', trigger: 'blur' },
            { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的参数值', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/sys/config/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            this.dataForm.paramKey = data.paramKey
            this.dataForm.paramValue = data.paramValue
            this.dataForm.remark = data.remark
          })
        }
      })
    },
      // 表单提交
    dataFormSubmit: Debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/sys/config`),
            method: this.dataForm.id ? 'put' : 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'paramKey': this.dataForm.paramKey,
              'paramValue': this.dataForm.paramValue,
              'remark': this.dataForm.remark
            })
          }).then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.visible = false
                this.$emit('refreshDataList')
              }
            })
          })
        }
      })
    })
  }
}
</script>
