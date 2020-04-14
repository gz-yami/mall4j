<template>
  <el-dialog
    :modal="false"
    title="选择发货地址"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="快递公司">
        <el-select v-model="dataForm.dvyId" placeholder="请选择">
          <el-option
            v-for="item in dataForm.dvyNames"
            :key="item.dvyId"
            :label="item.dvyName"
            :value="item.dvyId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="快递单号" prop="dvyFlowId">
        <el-input v-model="dataForm.dvyFlowId" controls-position="right" :min="0" label="快递单号"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          dvyId: '',
          dvyFlowId: 0,
          dvyNames: [],
          orderNumber: 0
        },
        dataRule: {
          dvyFlowId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (orderNumber, dvyId, dvyFlowId) {
        this.visible = true
        this.dataForm.orderNumber = orderNumber || ''
        this.dataForm.dvyId = dvyId || ''
        this.dataForm.dvyFlowId = dvyFlowId || ''
        this.$http({
          url: this.$http.adornUrl('/admin/delivery/list'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.dataForm.dvyNames = data
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/order/order/delivery`),
              method: 'put',
              data: this.$http.adornData({
                'orderNumber': this.dataForm.orderNumber,
                'dvyId': this.dataForm.dvyId,
                'dvyFlowId': this.dataForm.dvyFlowId
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
      }
    }
  }
</script>
