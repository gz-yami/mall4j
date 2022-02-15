<template>
  <el-dialog
    title="修改"
    :modal="false"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item v-if="dataForm.type !== 2" label="排序号" prop="seq">
        <el-input-number v-model="dataForm.seq" controls-position="right" :min="0" label="排序号"></el-input-number>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">下线</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
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
        addrOrderId: 0,
        receiver: '',
        province: '',
        mobile: ''
      }
    }
  },
  methods: {
    init (addrOrderId) {
      this.dataForm.addrOrderId = addrOrderId
      this.$http({
        url: this.$http.adornUrl(`/prod/category/listCategory/${this.dataForm.addrOrderId}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        this.dataForm = data
      }).then(() => {
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
        })
      })
    },
      // 表单提交
    dataFormSubmit: Debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/prod/category`),
            method: this.dataForm.currentId ? 'put' : 'post',
            data: this.$http.adornData({
              'categoryId': this.dataForm.currentId || undefined
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
