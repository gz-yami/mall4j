<template>
  <!-- 发货信息，用于导出代发货订单的excel交给快递公司 -->
  <el-dialog :modal="false"
             title="请输入发货信息"
             :close-on-click-modal="false"
             :visible.sync="visible"
             width="38%">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <el-form-item label="发件人姓名"
                    prop="consignmentName">
        <el-input v-model="dataForm.consignmentName"
                  controls-position="right"
                  label="发件人姓名"></el-input>
      </el-form-item>
      <el-form-item label="发货人手机号"
                    prop="consignmentMobile">
        <el-input v-model="dataForm.consignmentMobile"
                  controls-position="right"
                  label="发货人手机号"></el-input>
      </el-form-item>
      <el-form-item label="发货地址"
                    prop="consignmentAddr">
        <el-input v-model="dataForm.consignmentAddr"
                  controls-position="right"
                  label="发货地址"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button size="small" @click="visible = false">取消</el-button>
      <el-button type="primary" size = "small"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        consignmentName: '',
        consignmentMobile: '',
        consignmentAddr: ''
      },
      dataRule: {
        consignmentName: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        consignmentMobile: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        consignmentAddr: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (orderNumber, dvyId, dvyFlowId) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.visible = false
          this.$emit('inputCallback', this.dataForm)
        }
      })
    }
  }
}
</script>
