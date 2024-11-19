<template>
  <div class="components-tiny-mce">
    <div
      class="tinymce-container"
      :class="{ 'tox-fullscreen': toxFullscreen }"
    >
      <textarea
        :id="id"
        class="tinymce-textarea"
      />
      <!-- 增加图片区域 -->
      <div
        class="add-or-upload"
      >
        <el-upload
          class="upload-demo"
          list-type="picture"
          :action="uploadAction"
          :headers="uploadHeaders"
          :on-success="imageSuccessCBK"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
        >
          <el-button
            size="small"
            type="primary"
          >
            点击上传图片
          </el-button>
        </el-upload>
      </div>
    </div>
  </div>
</template>

<script setup>
import $cookie from 'vue-cookies'
import plugins from './plugins'
import toolbarPar from './toolbar'
import load from './dynamicLoadScript'

const uploadHeaders = { Authorization: $cookie.get('Authorization') }
const uploadAction = http.adornUrl('/admin/file/upload/element')

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  toolbar: {
    type: Array,
    required: false,
    default () {
      return []
    }
  },
  menubar: {
    type: String,
    default: 'file edit insert view format table'
  },
  id: {
    type: String,
    default: function () {
      return 'vue-tinymce-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
    }
  },
  height: {
    type: [Number, String],
    required: false,
    default: 360
  },
  width: {
    type: [Number, String],
    required: false,
    default: 'auto'
  }
})

const toxFullscreen = ref(false)
let hasInit = false
let hasChange = false
watch(() => props.modelValue, (val) => {
  if (!hasChange && hasInit) {
    setContent(val)
  }
})

const language = computed(() => {
  return localStorage.getItem('b2cLang') || 'zh_CN'
})
watch(() => language.value, () => {
  destroyTinymce()
  nextTick(() => initTinymce())
})

onMounted(() => {
  init()
})

onActivated(() => {
  if (window.tinymce) {
    initTinymce()
  }
})

onDeactivated(() => {
  destroyTinymce()
})

onUnmounted(() => {
  destroyTinymce()
})
const emit = defineEmits(['update:modelValue'])
const resourceCdn1 = new URL('/static/js/tinymce/js/tinymce/tinymce.min.js', import.meta.url).href
const init = () => {
  // dynamic load tinymce from cdn
  load(resourceCdn1, (err) => {
    if (!err) {
      initTinymce()
    }
  })
}

const tinymceId = ref(props.id)
const fullscreen = ref(false)
const initTinymce = () => {
  window.tinymce.init({
    language: language.value,
    selector: `#${tinymceId.value}`,
    height: props.height,
    body_class: 'panel-body ',
    object_resizing: false,
    toolbar: props.toolbar.length > 0 ? props.toolbar : toolbarPar,
    menubar: props.menubar,
    plugins,
    end_container_on_empty_block: true,
    powerpaste_word_import: 'clean',
    paste_enable_default_filters: false, // 从word复制的内容保持原格式到富文本
    code_dialog_height: 450,
    code_dialog_width: 1000,
    content_style: 'body {-webkit-user-modify: read-write;overflow-wrap: break-word;-webkit-line-break: after-white-space;}img {max-width: 100%;vertical-align:initial}',
    advlist_bullet_styles: 'square',
    advlist_number_styles: 'default',
    imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
    default_link_target: '_blank',
    link_title: false,
    nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
    init_instance_callback: (editor) => {
      if (props.modelValue) {
        editor.setContent(props.modelValue)
      }
      hasInit = true
      editor.on('NodeChange Change KeyUp SetContent', () => {
        hasChange = true
        emit('update:modelValue', editor.getContent())
      })
    },
    setup: (editor) => {
      editor.on('FullscreenStateChanged', (e) => {
        fullscreen.value = e.state
      })
    },
    convert_urls: false
  })
}

const destroyTinymce = () => {
  try {
    const tinymce = window.tinymce.get(tinymceId.value)
    if (fullscreen.value) {
      tinymce.execCommand('mceFullScreen')
    }
    if (tinymce) {
      tinymce.destroy()
    }
  } catch (e) { }
}
const setContent = (value) => {
  if (window.tinymce) {
    window.tinymce.get(tinymceId.value).setContent(value || '')
  }
}
const resourcesUrl = import.meta.env.VITE_APP_RESOURCES_URL
// eslint-disable-next-line no-unused-vars
const imageSuccessCBK = (response, file, fileList) => {
  window.tinymce.get(props.id).insertContent(`<img alt="" src="${resourcesUrl + file.response.data}" >`)
}

/**
 * 限制图片上传大小
 */
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg'
  if (!isJPG) {
    this.$message.error('上传图片只能是jpeg/jpg/png/gif 格式!')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    this.$message.error('上传图片大小不能超过 2MB!')
  }
  return isLt2M && isJPG
}
</script>
<!--eslint-disable-next-line vue-scoped-css/enforce-style-type -->
<style lang="scss">
.components-tiny-mce {
  .tox-fullscreen .add-or-upload {
    z-index: 9999 !important;
    position:fixed !important;
    right: 0;
    top: 0;

  }
  .tinymce-container {
    position: relative;
    .add-or-upload {
      z-index: 999;
      position: absolute;
      top: 10px;
      right: 10px;
    }
  }
}
.tox-tinymce-aux {
  z-index: 9999 !important;
}
</style>
