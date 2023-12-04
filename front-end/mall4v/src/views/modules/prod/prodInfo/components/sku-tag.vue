<template>
  <div class="mod-prod-sku-tag">
    <el-form-item label="商品规格">
      <el-button @click="shopTagInput()">
        添加规格
      </el-button>
      <div
        v-for="(tag, tagIndex) in skuTags"
        :key="tagIndex"
      >
        <span>{{ tag.tagName }}</span>
        <el-button
          class="button-new-tag"
          type="text"
          icon="el-icon-delete"
          @click="removeTag(tagIndex)"
        >
          删除
        </el-button>
        <br>
        <el-tag
          v-for="(tagItem, tagItemIndex) in tag.tagItems"
          :key="tagItem.valueId"
          closable
          :disable-transitions="false"
          @close="handleTagClose(tagIndex, tagItemIndex)"
        >
          {{ tagItem.propValue }}
        </el-tag>
        <el-input
          v-if="tagItemInputs[tagIndex] && tagItemInputs[tagIndex].visible"
          :ref="`saveTagInput${tagIndex}`"
          v-model="tagItemInputs[tagIndex].value"
          class="input-new-tag"
          @keyup.enter="handleInputConfirm(tagIndex)"
          @blur="handleInputConfirm(tagIndex)"
        />
        <el-button
          v-else
          class="button-new-tag"
          @click="showTagInput(tagIndex)"
        >
          + 添加
        </el-button>
      </div>
    </el-form-item>
    <el-form-item
      v-show="isShowTagInput"
      label="规格名"
    >
      <el-col :span="8">
        <el-select
          v-model="addTagInput.propName"
          filterable
          allow-create
          default-first-option
          placeholder="请选择"
          @change="handleTagClick"
        >
          <el-option
            v-for="item in unUseTags"
            :key="item.propId"
            :label="item.propName"
            :value="item.propName"
          />
        </el-select>
      </el-col>
    </el-form-item>
    <el-form-item
      v-show="isShowTagInput"
      label="规格值"
    >
      <el-col :span="8">
        <el-select
          v-model="addTagInput.selectValues"
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="请选择"
        >
          <el-option
            v-for="item in dbTagValues"
            :key="item.valueId"
            :label="item.propValue"
            :value="item.propValue"
          />
        </el-select>
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button
        v-show="isShowTagInput"
        type="primary"
        @click="addTag()"
      >
        确定
      </el-button>
      <el-button
        v-show="isShowTagInput"
        @click="hideTagInput()"
      >
        取消
      </el-button>
    </el-form-item>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { scoreProdStore } from '@/stores/prod.js'
const prod = scoreProdStore()
const props = defineProps({
  skuList: {
    type: Array,
    default: () => []
  }
})
const emit = defineEmits(['change'])
const isShowTagInput = ref(false)
const addTagInput = ref({
  propName: '',
  selectValues: []
})
const type = ref(0)
const tagItemName = ref('')
const tagItemInputs = ref([])
const dbTagValues = ref([]) // 根据选定的规格所查询出来的规格值
const dbTags = ref([]) // 数据库中的规格

let tagName = ''
let tagNameIndex = 0
let maxValueId = 0 // 规格值id最大
let maxPropId = 0 // 规格id 最大
let initing = false

const skuTags = computed({
  get () { return prod.skuTags },
  set (val) { prod.updateSkuTags(val) }
})

/**
 * 未使用的规格, 通过计算属性计算
 */
const unUseTags = computed(() => {
  const res = []
  for (let i = 0; i < dbTags.value.length; i++) {
    const dbTag = dbTags.value[i]
    const specIndex = skuTags.value?.findIndex(tag => tag.tagName === dbTag.propName)
    if (specIndex === -1) {
      res.push(dbTag)
    }
  }
  return res
})

const defalutSku = computed(() => {
  return prod.defalutSku
})

watch(() => skuTags.value,
  (val) => {
    if (initing) {
      initing = false
      return
    }
    let skuListArr = []
    if (type.value === 4) {
      // 删除规格值
      props.skuList?.forEach(sku => {
        const propertiesArray = sku.properties.split(';')
        if (tagItemName.value !== propertiesArray[tagNameIndex].split(':')[1]) {
          skuListArr.push(sku)
        }
      })
    } else if (type.value === 2) {
      // 添加规格值
      const properties = tagName + ':' + tagItemName.value
      // 增加或删除规格
      let tempSkuList = []
      val?.forEach(tag => {
        if (skuListArr.length === 0) {
          if (tagName === tag.tagName) {
            const sku = Object.assign({}, defalutSku.value)
            sku.properties = properties // 销售属性组合字符串
            skuListArr.push(sku)
          } else {
            tag.tagItems.forEach(tagItem => {
              const sku = Object.assign({}, defalutSku.value)
              sku.properties = `${tag.tagName}:${tagItem.propValue}` // 销售属性组合字符串
              skuListArr.push(sku)
            })
          }
          if (val.length === 1) {
            skuListArr = props.skuList.concat(skuListArr)
          }
        } else {
          tempSkuList = []
          if (tagName === tag.tagName) {
            skuListArr.forEach(sku => {
              if (sku.properties.indexOf(tagName) === -1) {
                const newSku = Object.assign({}, sku)
                newSku.properties = `${sku.properties};${properties}`
                tempSkuList.push(newSku)
              }
            })
          } else {
            tag.tagItems.forEach(tagItem => {
              skuListArr.forEach(sku => {
                if (sku.properties.indexOf(tag.tagName) === -1) {
                  const newSku = Object.assign({}, sku)
                  newSku.properties = `${sku.properties};${tag.tagName}:${tagItem.propValue}`
                  tempSkuList.push(newSku)
                }
              })
            })
          }
          skuListArr = props.skuList.concat(tempSkuList)
        }
      })
    } else {
      // 增加或删除规格
      let tempSkuList = []
      val?.forEach(tag => {
        if (skuListArr.length === 0) {
          tag.tagItems.forEach(tagItem => {
            const sku = Object.assign({}, defalutSku.value)
            sku.properties = `${tag.tagName}:${tagItem.propValue}` // 销售属性组合字符串
            skuListArr.push(sku)
          })
        } else {
          tempSkuList = []
          tag.tagItems.forEach(tagItem => {
            skuListArr.forEach(sku => {
              const newSku = Object.assign({}, sku)
              newSku.properties = `${sku.properties};${tag.tagName}:${tagItem.propValue}`
              tempSkuList.push(newSku)
            })
          })
          skuListArr = tempSkuList
        }
      })
    }
    if (!skuListArr.length) {
      skuListArr.push(Object.assign({}, defalutSku.value))
    }
    // debugger
    emit('change', skuListArr)
  },
  {
    deep: true
  }
)

onMounted(() => {
  http({
    url: http.adornUrl('/prod/spec/list'),
    method: 'get',
    params: http.adornParams()
  })
    .then(({ data }) => {
      dbTags.value = data
      if (data) {
        maxPropId = Math.max.apply(Math, data.map(item => item.propId))
      } else {
        maxPropId = 0
      }
    })
  http({
    url: http.adornUrl('/prod/spec/listSpecMaxValueId'),
    method: 'get',
    params: http.adornParams()
  })
    .then(({ data }) => {
      if (data) {
        maxValueId = data
      } else {
        maxValueId = 0
      }
    })
})

const init = (skuList) => {
  if (!skuList || !skuList.length) {
    skuTags.value = []
    emit('change', [Object.assign({}, defalutSku.value)])
    return
  }
  initing = true
  const skuTagsParam = []
  for (let i = 0; i < skuList.length; i++) {
    const sku = skuList[i]
    if (!sku.properties) break
    const propertiesArray = sku.properties.split(';')
    for (const j in propertiesArray) {
      const properties = propertiesArray[j].split(':')
      if (!skuTagsParam[j]) {
        skuTagsParam[j] = {
          tagName: properties[0],
          tagItems: []
        }
        tagItemInputs.value.push({ visible: false, value: '' })
      }
      const tagItemNameIndex = skuTagsParam[j].tagItems.findIndex((tagItemName) => tagItemName.propValue === properties[1])
      if (tagItemNameIndex === -1) {
        skuTagsParam[j].tagItems.push({ propValue: properties[1] })
      }
    }
  }
  skuTags.value = skuTagsParam
}
defineExpose({ init })

/**
 * 显示规格名、规格值输入框
 */
const shopTagInput = () => {
  isShowTagInput.value = true
}

/**
 * 隐藏规格名、规格值输入框
 */
const hideTagInput = () => {
  isShowTagInput.value = false
  cleanTagInput()
}
const addTag = () => {
  const selectValues = addTagInput.value.selectValues
  if (!addTagInput.value.propName) {
    ElMessage.error('请输入规格名')
    return
  }
  if (!selectValues.length) {
    ElMessage.error('请输入规格值')
    return
  }
  isShowTagInput.value = false
  for (let i = 0; i < selectValues.length; i++) {
    const element = selectValues[i]
    const is = Object.prototype.toString.call(element) === '[object Object]'
    if (!is) {
      maxPropId = maxPropId + 1
      break
    }
  }
  const tagItems = []
  for (let i = 0; i < selectValues.length; i++) {
    const element = selectValues[i]
    const is = Object.prototype.toString.call(element) === '[object Object]'
    if (is) {
      tagItems.push(element)
    } else {
      maxValueId = maxValueId + 1
      tagItems.push({ propId: maxPropId, propValue: element, valueId: maxValueId })
    }
  }
  // 向规格中放入规格输入框内的数据
  prod.addSkuTag({
    tagName: addTagInput.value.propName,
    tagItems
  })
  type.value = 1
  cleanTagInput()
}

/**
 * 清除规格值输入框内容
 */
const cleanTagInput = () => {
  addTagInput.value = {
    propName: '',
    selectValues: []
  }
  dbTagValues.value = []
}

/**
 * 规格名输入框，选中规格事件
 */
const handleTagClick = () => {
  // 清空规格值输入框
  dbTagValues.value = []
  addTagInput.value.selectValues = []
  // 判断规格名输入的值是否为数据库中已有的值
  const specsIndex = dbTags.value?.findIndex(spec => spec.propName === addTagInput.value.propName)
  // 如果不是，则说明为用户随便输入
  if (specsIndex === -1) return
  // 如果数据库已有该规格名，则获取根据id获取该规格名称含有的规格值
  http({
    url: http.adornUrl(`/prod/spec/listSpecValue/${dbTags.value[specsIndex].propId}`),
    method: 'get',
    params: http.adornParams()
  }).then(({ data }) => {
    dbTagValues.value = data
  })
}

/**
 * 规格名输入框，选中规格事件
 */
const handleTagClose = (tagIndex, tagItemIndex) => {
  tagName = skuTags.value[tagIndex].tagName
  tagNameIndex = tagIndex
  tagItemName.value = skuTags.value[tagIndex].tagItems[tagItemIndex].propValue
  if (skuTags.value[tagIndex].tagItems.length === 1) {
    return
  }
  type.value = 4
  prod.removeSkuTagItem(tagIndex, tagItemIndex)
}

/**
 * 标签输入框确定时调用
 */
const handleInputConfirm = (tagIndex) => {
  if (checkTagItem(tagIndex)) {
    return
  }
  const tagItems = skuTags.value[tagIndex].tagItems
  const itemValue = tagItemInputs.value[tagIndex].value
  const index = tagItems.length - 1
  tagName = skuTags.value[tagIndex].tagName
  tagItemName.value = tagItemInputs.value[tagIndex].value
  const maxValue = getMaxValueId(skuTags.value[tagIndex].tagItems)
  const tagItem = { propId: index === -1 ? 0 : skuTags.value[tagIndex].tagItems[index].propId, propValue: itemValue, valueId: index === -1 ? 0 : (maxValue + 1) }
  if (tagItem) {
    prod.addSkuTagItem({ tagIndex, tagItem })
  }
  tagItemInputs.value[tagIndex].visible = false
  tagItemInputs.value[tagIndex].value = ''
  type.value = 2
}

/**
 * 显示标签输入框
 */
const showTagInput = (tagIndex) => {
  tagItemInputs.value.push({ visible: false, value: '' })
  tagItemInputs.value[tagIndex].visible = true
  nextTick(() => {
    [`saveTagInput${tagIndex}`][0].value.input.focus()
  })
}

/**
 * 获取数据集合所有对象中某个属性的最大值
 */
const getMaxValueId = (list) => {
  return Math.max.apply(Math, list.map(item => item.valueId))
}

/**
 * 删除 规格
 * @param tagIndex
 */
const removeTag = (tagIndex) => {
  type.value = 3
  prod.removeSkuTag(tagIndex)
}

/**
 * 新增规格值时，判断是否存在同名的规格值
 */
const checkTagItem = (tagIndex) => {
  const tagItem = tagItemInputs.value[tagIndex].value
  if (!tagItem) {
    tagItemInputs.value[tagIndex].visible = false
    tagItemInputs.value[tagIndex].value = ''
    return true
  }
  let isSame = false
  skuTags.value?.forEach(tag => {
    const arr = tag.tagItems.map((item) => {
      return item.propValue
    })
    if (arr.indexOf(tagItem) > -1) {
      isSame = true
      ElMessage.error('product.specificationValue')
      return false
    }
  })
  return isSame
}
</script>

<style lang="scss" scoped>
.mod-prod-sku-tag {
  :deep(.el-tag + .el-tag) {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
}

// 新增规格外部边框
:deep(.sku-border) {
  border: 1px solid #EBEEF5;
  width:70%
}
:deep(.sku-background){
  background-color: #F6f6f6;
  margin: 12px 12px;
  .el-button{
    margin-left: 10px;
    span{
      color:#000 !important;
    }
  }
  .el-form-item__label{
    padding:0 24px 0 0
  }
}
:deep(.sku-tag){
  margin: 12px 12px;
}
:deep(.tagTree){
  margin-left: 18px;
  padding-bottom:8px;
}
.el-form-item__content div {
  width: 100%;
}
</style>
