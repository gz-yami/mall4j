<template>
  <image
    v-if="!isError && imgPath"
    :src="imgPath"
    :style="imgStyle"
    :class="classList"
    :mode="imgMode"
    @error="imgError"
    @load="imgLoad"
    @tap="handleTap"
  />
  <image
    v-else
    :src="defaultImgPath"
    :style="imgStyle"
    :class="classList"
    @tap="handleTap"
  />
</template>

<script setup>
const props = defineProps({
  src: {
    type: String,
    default: ''
  },
  imgMode: {
    type: String,
    default: 'scaleToFill'
  },
  classList: {
    type: Array,
    default: () => {
      return []
    }
  },
  imgStyle: {
    type: Object,
    default: () => {
      return {}
    }
  },
  // 默认失败图片类型, false为默认图片, true为默认头像图片
  defaultImgType: {
    type: Boolean,
    default: false
  }
})

const imgPath = computed(() => {
  return util.checkFileUrl(props.src)
})
const defaultImgPath = computed(() => {
  if (props.defaultImgType) return '/static/images/icon/head04.png'
  return '/static/images/icon/def.png'
})

const emit = defineEmits(['imgError', 'imgLoad', 'handleTap'])
const isError = ref(false)
const imgError = () => {
  isError.value = true
  emit('imgError')
}

const imgLoad = (e) => {
  emit('imgLoad', e)
}

const handleTap = () => {
  emit('handleTap')
}
</script>

<style scoped>
image {
  width: 100%;
  height: 100%;
}
</style>
