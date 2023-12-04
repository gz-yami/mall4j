<template>
  <div>
    <el-upload
      class="pic-uploader-component"
      :action="uploadAction"
      :headers="uploadHeaders"
      accept=".png,.jpg,.jpeg,.gif"
      :show-file-list="false"
      :on-success="handleUploadSuccess"
      :before-upload="beforeAvatarUpload"
    >
      <img
        v-if="modelValue"
        alt=""
        :src="resourcesUrl + modelValue"
        class="pic"
      >
      <el-icon
        v-else
        color="#8c939d"
        size="28"
      >
        <Plus />
      </el-icon>
    </el-upload>
  </div>
</template>

<script setup>
import $cookie from 'vue-cookies'
import { ElMessage } from 'element-plus'
const uploadHeaders = { Authorization: $cookie.get('Authorization') }
const uploadAction = http.adornUrl('/admin/file/upload/element')
const emit = defineEmits(['update:modelValue'])
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  modelValue: {
    default: '',
    type: String
  }
})
const resourcesUrl = import.meta.env.VITE_APP_RESOURCES_URL
/**
 * 图片上传
 */
// eslint-disable-next-line no-unused-vars
const handleUploadSuccess = (response, file) => {
  emit('update:modelValue', file.response.data)
}

/**
 * 限制图片上传大小
 */
const beforeAvatarUpload = (file) => {
  const isLt2M = file.size / 1024 / 1024 < 2
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg'
  if (!isJPG) {
    ElMessage.error('上传图片只能是jpeg/jpg/png/gif 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isLt2M && isJPG
}

</script>

<style lang="scss" scoped>
.pic-uploader-component :deep(.el-upload) {
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
.pic-uploader-component :deep(.el-upload:hover) {
  border-color: #409EFF;
}
:deep(.el-upload) {
  width: 148px;
  height: 148px;
}
</style>
