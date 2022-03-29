<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="100px">
      <el-form-item label="bean名称" prop="beanName">
        <el-input v-model="dataForm.beanName" placeholder="spring bean名称, 如: testTask"></el-input>
      </el-form-item>
      <el-form-item label="方法名称" prop="methodName">
        <el-input v-model="dataForm.methodName" placeholder="方法名称"></el-input>
      </el-form-item>
      <el-form-item label="参数" prop="params">
        <el-input v-model="dataForm.params" placeholder="参数"></el-input>
      </el-form-item>
      <el-form-item label="cron表达式" prop="cronExpression">
        <el-input v-model="dataForm.cronExpression" placeholder="如: 0 0 12 * * ?"></el-input>
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
          beanName: '',
          methodName: '',
          params: '',
          cronExpression: '',
          remark: '',
          status: 0
        },
        dataRule: {
          beanName: [
            { required: true, message: 'beanName不能为空', trigger: 'blur' }
          ],
          methodName: [
            { required: true, message: '方法名称不能为空', trigger: 'blur' }
          ],
          cronExpression: [
            { required: true, message: 'cron表达式不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/sys/schedule/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              this.dataForm.beanName = data.beanName
              this.dataForm.methodName = data.methodName
              this.dataForm.params = data.params
              this.dataForm.cronExpression = data.cronExpression
              this.dataForm.remark = data.remark
              this.dataForm.status = data.status
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit: Debounce(function () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/sys/schedule`),
              method: this.dataForm.id ? 'put' : 'post',
              data: this.$http.adornData({
                'jobId': this.dataForm.id || undefined,
                'beanName': this.dataForm.beanName,
                'methodName': this.dataForm.methodName,
                'params': this.dataForm.params,
                'cronExpression': this.dataForm.cronExpression,
                'remark': this.dataForm.remark,
                'status': !this.dataForm.id ? undefined : this.dataForm.status
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
