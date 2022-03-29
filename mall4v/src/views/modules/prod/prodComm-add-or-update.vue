<template>
  <el-dialog :title="!dataForm.prodCommId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">

      <div v-if="!isEdit">
        <el-form-item label="评论内容"
                      prop="userName">
          <el-input type="textarea"
                    :readonly='true'
                    v-model="dataForm.content"></el-input>
        </el-form-item>

        <el-form-item label="评论图片"
                      prop="userName">
          <div v-if="dataForm.pics==null || dataForm.pics.length>0">
            无
          </div>
          <img max-width="100%"
               v-else
               v-for="item in dataForm.pics"
               :key="item"
               :src="dialogImageUrl + item">
        </el-form-item>

        <el-form-item label="记录时间"
                      prop="userName">
          <el-input v-model="dataForm.recTime"
                    :readonly='true'></el-input>
        </el-form-item>
        <el-form-item label="回复时间"
                      prop="userName"
                      :readonly='true'>
          <el-input v-model="dataForm.replyTime"
                    :readonly='true'></el-input>
        </el-form-item>
        <el-form-item label="IP来源"
                      prop="userName">
          <el-input v-model="dataForm.postip"
                    :readonly='true'></el-input>
        </el-form-item>
        <el-form-item label="得分"
                      prop="score">
          <el-input v-model="dataForm.score"
                    :readonly='true'></el-input>
        </el-form-item>

        <el-form-item label="是否匿名"
                      size="mini"
                      prop="isAnonymous">
          <el-radio-group v-model="dataForm.isAnonymous"
                          :disabled='true'>
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">不是</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>

      <el-form-item label="掌柜回复"
                    type="textarea"
                    prop="userName">
        <el-input v-model="dataForm.replyContent"
                  :readonly='!isEdit'></el-input>
      </el-form-item>

      <el-form-item label="审核"
                    size="mini"
                    prop="status"
                    v-if="isEdit">
        <el-radio-group v-model="dataForm.status"
                        :readonly='true'>
          <el-radio :label="1">审核通过</el-radio>
          <el-radio :label="-1">不通过</el-radio>
          <el-radio :label="0">等待审核</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary"
                 v-if="isEdit"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      isEdit: false,
      visible: false,
      roleList: [],
      dataForm: {
        prodCommId: null,
        prodId: null,
        orderItemId: null,
        userId: null,
        content: null,
        replyContent: null,
        recTime: null,
        replyTime: null,
        replySts: null,
        postip: null,
        score: null,
        usefulCounts: null,
        photoJson: null,
        isAnonymous: null,
        status: null
      },
      dataRule: {
      },
      resourcesUrl: process.env.VUE_APP_RESOURCES_URL
    }
  },
  methods: {
    init (prodCommId, isEdit) {
      this.isEdit = isEdit
      this.dataForm.prodCommId = prodCommId || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.prodCommId) {
          this.$http({
            url: this.$http.adornUrl('/prod/prodComm/info/' + this.dataForm.prodCommId),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            this.dataForm = data
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/prod/prodComm'),
            method: this.dataForm.prodCommId ? 'put' : 'post',
            data: this.$http.adornData(this.dataForm)
          }).then(({ data }) => {
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
