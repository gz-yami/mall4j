<template>
  <div>
    <el-upload
      class="pic-uploader-component"
      :action="$http.adornUrl('/admin/file/upload/element')"
      :headers="{Authorization: $cookie.get('Authorization')}"
      :show-file-list="false"
      :on-success="handleUploadSuccess"
      :before-upload="beforeAvatarUpload">
      <img v-if="value" :src="resourcesUrl + value" class="pic">
      <i v-else class="el-icon-plus pic-uploader-icon"></i>
    </el-upload>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        resourcesUrl: process.env.VUE_APP_RESOURCES_URL
      }
    },
    props: {
      value: {
        default: '',
        type: String
      }
    },
    methods: {
      // 图片上传
      handleUploadSuccess (response, file, fileList) {
        this.$emit('input', file.response)
      },
      // 限制图片上传大小
      beforeAvatarUpload (file) {
        const isLt2M = file.size / 1024 / 1024 < 2
        const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg'
        if (!isJPG) {
          this.$message.error('上传图片只能是jpeg/jpg/png/gif 格式!')
        }
        if (!isLt2M) {
          this.$message.error('上传图片大小不能超过 2MB!')
        }
        return isLt2M && isJPG
      }
    }
  }
</script>

<style lang="scss">
  .pic-uploader-component .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    .pic-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }
    .pic {
      width: 178px;
      height: 178px;
      display: block;
    }
  }
  .pic-uploader-component .el-upload:hover {
    border-color: #409EFF;
  }

</style>
