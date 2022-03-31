<template>
  <div>
    <el-upload
      :action="$http.adornUrl('/admin/file/upload/element')"
      :headers="{Authorization: $cookie.get('Authorization')}"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :file-list="imageList"
      :before-upload="beforeAvatarUpload">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        dialogImageUrl: '',
        dialogVisible: false,
        resourcesUrl: process.env.VUE_APP_RESOURCES_URL
      }
    },
    props: {
      value: {
        default: '',
        type: String
      }
    },
    computed: {
      imageList () {
        let res = []
        if (this.value) {
          let imageArray = this.value.split(',')
          for (let i = 0; i < imageArray.length; i++) {
            res.push({url: this.resourcesUrl + imageArray[i], response: imageArray[i]})
          }
        }
        this.$emit('input', this.value)
        return res
      }
    },
    methods: {
      // 图片上传
      handleUploadSuccess (response, file, fileList) {
        let pics = fileList.map(file => {
          return file.response
        }).join(',')
        this.$emit('input', pics)
      },
      // 限制图片上传大小
      beforeAvatarUpload (file) {
        const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg'
        if (!isJPG) {
          this.$message.error('上传图片只能是jpeg/jpg/png/gif 格式!')
        }
        const isLt2M = file.size / 1024 / 1024 < 2
        if (!isLt2M) {
          this.$message.error('上传图片大小不能超过 2MB!')
        }
        return isLt2M && isJPG
      },
      handleRemove (file, fileList) {
        let pics = fileList.map(file => {
          return file.response
        }).join(',')
        this.$emit('input', pics)
      },
      handlePictureCardPreview (file) {
        this.dialogImageUrl = file.url
        this.dialogVisible = true
      }
    }
  }
</script>

