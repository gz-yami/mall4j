<template>
  <div>
    <el-upload
      :action="uploadAction"
      :headers="uploadHeaders"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :file-list="imageList"
      :before-upload="beforeAvatarUpload"
    >
      <el-icon><Plus /></el-icon>
    </el-upload>
    <el-dialog v-model="dialogVisible">
      <img
        width="100%"
        :src="dialogImageUrl"
        alt=""
      >
    </el-dialog>
  </div>
</template>

<script setup>
import $cookie from 'vue-cookies'
import { ElMessage } from 'element-plus'

const uploadHeaders = { Authorization: $cookie.get('Authorization') }
const uploadAction = http.adornUrl('/admin/file/upload/element')
const props = defineProps({
  modelValue: {
    default: '',
    type: String
  }
})
const emit = defineEmits(['update:modelValue'])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const resourcesUrl = import.meta.env.VITE_APP_RESOURCES_URL

const imageList = computed(() => {
  const res = []
  if (props.modelValue) {
    const imageArray = props.modelValue?.split(',')
    for (let i = 0; i < imageArray.length; i++) {
      res.push({ url: resourcesUrl + imageArray[i], response: imageArray[i] })
    }
  }
  emit('update:modelValue', props.modelValue)
  return res
})

/**
 * 图片上传
 */
// eslint-disable-next-line no-unused-vars
const handleUploadSuccess = (response, file, fileList) => {
  const pics = fileList.map(file => {
    if (typeof file.response === 'string') {
      return file.response
    }
    return file.response.data
  }).join(',')
  emit('update:modelValue', pics)
}

/**
 * 限制图片上传大小
 */
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg'
  if (!isJPG) {
    ElMessage.error('上传图片只能是jpeg/jpg/png/gif 格式!')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isLt2M && isJPG
}

// eslint-disable-next-line no-unused-vars
const handleRemove = (file, fileList) => {
  const pics = fileList.map(file => {
    if (typeof file.response === 'string') {
      return file.response
    }
    return file.response.data
  }).join(',')
  emit('update:modelValue', pics)
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

</script>
