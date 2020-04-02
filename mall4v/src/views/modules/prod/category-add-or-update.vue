<template>
  <el-dialog :title="!dataForm.currentId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item v-if="dataForm.type !== 2"
                    label="分类图片"
                    prop="pic">
        <pic-upload v-model="dataForm.pic"></pic-upload>
      </el-form-item>
      <el-form-item v-if="dataForm.type !== 2"
                    label="分类名称"
                    prop="categoryName">
        <el-input v-model="dataForm.categoryName"
                  controls-position="right"
                  :min="0"
                  label="分类名称"></el-input>
      </el-form-item>
      <el-form-item label="上级分类">
        <el-cascader expand-trigger="hover"
                     :options="categoryList"
                     :props="categoryTreeProps"
                     change-on-select
                     :clearable="true"
                     v-model="selectedCategory"
                     @change="handleChange">
        </el-cascader>
      </el-form-item>
      <el-form-item v-if="dataForm.type !== 2"
                    label="排序号"
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
      <el-button size="small"
                 @click="visible = false">取消</el-button>
      <el-button size="small"
                 type="primary"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { treeDataTranslate, idList } from '@/utils'
import PicUpload from '@/components/pic-upload'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        categoryId: 0,
        currentId: 0,
        grade: 0,
        categoryName: '',
        seq: 1,
        status: 1,
        parentId: 0,
        pic: ''
      },
      dataRule: {
        categoryName: [
          { required: true, message: '分类名称不能为空', trigger: 'blur' }
        ],
        pic: [
          { required: true, message: '分类图片不能为空', trigger: 'blur' }
        ]
      },
      categoryList: [],
      selectedCategory: [],
      categoryTreeProps: {
        value: 'categoryId',
        label: 'categoryName'
      }
    }
  },
  components: {
    PicUpload
  },
  methods: {
    init (id) {
      this.dataForm.currentId = id || 0
      this.dataForm.categoryId = id || 0
      this.$http({
        url: this.$http.adornUrl('/prod/category/listCategory'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        this.categoryList = treeDataTranslate(data, 'categoryId', 'parentId')
      }).then(() => {
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          this.selectedCategory = []
        })
      }).then(() => {
        if (this.dataForm.categoryId) {
          // 修改
          this.$http({
            url: this.$http.adornUrl(`/prod/category/info/${this.dataForm.categoryId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            this.dataForm.categoryId = data.categoryId
            this.dataForm.categoryName = data.categoryName
            this.dataForm.seq = data.seq
            this.dataForm.pic = data.pic
            this.dataForm.parentId = data.parentId
            this.dataForm.status = data.status
            this.selectedCategory = idList(this.categoryList, data.parentId, 'categoryId', 'children').reverse()
          })
        }
      })
    },
    handleChange (val) {
      this.dataForm.parentId = val[val.length - 1]
    },
    // 表单提交
    dataFormSubmit () {
      if (this.selectedCategory.length === 1) {
        this.dataForm.grade = 0
      }
      if (this.selectedCategory.length === 2) {
        this.dataForm.grade = 1
      }
      if (this.selectedCategory.length === 3) {
        this.dataForm.grade = 2
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/prod/category`),
            method: this.dataForm.categoryId ? 'put' : 'post',
            data: this.$http.adornData({
              'categoryId': this.dataForm.categoryId || undefined,
              'categoryName': this.dataForm.categoryName,
              'status': this.dataForm.status,
              'seq': this.dataForm.seq,
              'grade': this.dataForm.grade,
              'parentId': this.dataForm.parentId,
              'pic': this.dataForm.pic
            })
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
