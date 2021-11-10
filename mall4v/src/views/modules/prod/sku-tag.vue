<template>
  <div class="mod-prod-sku-tag">
    <el-form-item label="商品规格">
      <el-button size="mini" @click="shopTagInput()">添加规格</el-button>
      <div v-for="(tag, tagIndex) in skuTags" :key="tagIndex">
        <span>{{tag.tagName}}</span>
        <el-button class="button-new-tag" type="text"  icon="el-icon-delete" @click="removeTag(tagIndex)">删除</el-button>
        <br/>
        <el-tag
          v-for="(tagItem, tagItemIndex) in tag.tagItems"
          :key="tagItem.valueId"
          closable
          :disable-transitions="false"
          @close="handleTagClose(tagIndex, tagItemIndex)">
          {{tagItem.propValue}}
        </el-tag>
        <el-input
          class="input-new-tag"
          v-if="tagItemInputs[tagIndex] && tagItemInputs[tagIndex].visible"
          v-model="tagItemInputs[tagIndex].value"
          :ref="`saveTagInput${tagIndex}`"
          size="small"
          @keyup.enter.native="handleInputConfirm(tagIndex)"
          @blur="handleInputConfirm(tagIndex)">
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showTagInput(tagIndex)">+ 添加</el-button>
      </div>
    </el-form-item>
    <el-form-item label="规格名" v-show="isShowTagInput">
      <el-col :span="8">
        <el-select v-model="addTagInput.propName" filterable allow-create default-first-option placeholder="请选择" @change="handleTagClick">
          <el-option
            v-for="item in unUseTags"
            :key="item.propId"
            :label="item.propName"
            :value="item.propName">
          </el-option>
        </el-select>
      </el-col>
    </el-form-item>
    <el-form-item label="规格值" v-show="isShowTagInput">
      <el-col :span="8">
        <el-select v-model="addTagInput.selectValues" multiple filterable allow-create default-first-option placeholder="请选择">
          <el-option
            v-for="item in dbTagValues"
            :key="item.valueId"
            :label="item.propValue"
            :value="item.propValue">
          </el-option>
        </el-select>
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button size="mini" type="primary" @click="addTag()" v-show="isShowTagInput">确定</el-button>
      <el-button size="mini" @click="hideTagInput()" v-show="isShowTagInput">取消</el-button>
    </el-form-item>


  </div>
</template>

<script>
export default {
  data () {
    return {
      value: [],
      isShowTagInput: false,
      addTagInput: {
        propName: '',
        selectValues: []
      },
      type: 0,
      tagItemName: '',
      tagName: '',
      tagNameIndex: 0,
      tagItemInputs: [],
      // sku的标记
      // tags: [],
      // 数据库中的规格
      dbTags: [],
      // 根据选定的规格所查询出来的规格值
      dbTagValues: [],
      specs: [], // 使用的规格
      maxValueId: 0, // 规格值id最大
      maxPropId: 0, // 规格id 最大
      initing: false
    }
  },
  created: function () {
    this.$http({
      url: this.$http.adornUrl(`/prod/spec/list`),
      method: 'get',
      params: this.$http.adornParams()
    }).then(({data}) => {
      this.dbTags = data
      if (data) {
        this.maxPropId = Math.max.apply(Math, data.map(item => { return item.propId }))
      } else {
        this.maxPropId = 0
      }
    })
    this.$http({
      url: this.$http.adornUrl(`/prod/spec/listSpecMaxValueId`),
      method: 'get',
      params: this.$http.adornParams()
    }).then(({ data }) => {
      if (data) {
        this.maxValueId = data
      } else {
        this.maxValueId = 0
      }
    })
  },
  props: {
  //   tags: { // sku的标记
  //     default: [],
  //     type: Array
  //   }
  // },
    skuList: {
      default: []
    }
  },
  computed: {
    // 未使用的规格, 通过计算属性计算
    unUseTags () {
      let res = []
      for (let i = 0; i < this.dbTags.length; i++) {
        const dbTag = this.dbTags[i]
        let specIndex = this.skuTags.findIndex(tag => tag.tagName === dbTag.propName)
        if (specIndex === -1) {
          res.push(dbTag)
        }
      }
      return res
    },
    skuTags: {
      get () { return this.$store.state.prod.skuTags },
      set (val) { this.$store.commit('prod/updateSkuTags', val) }
    },
    defalutSku () {
      return this.$store.state.prod.defalutSku
    }
  },
  watch: {
    skuTags: {
      handler (val, oldVal) {
        if (this.initing) {
          this.initing = false
          return
        }
        let skuList = []
        if (this.type === 4) {
        // 删除规格值
          this.skuList.forEach(sku => {
            let propertiesArray = sku.properties.split(';')
            if (this.tagItemName !== propertiesArray[this.tagNameIndex].split(':')[1]) {
              skuList.push(sku)
            }
          })
        } else if (this.type === 2) {
        // 添加规格值
          var properties = this.tagName + ':' + this.tagItemName
        // 增加或删除规格
          let tempSkuList = []
          val.forEach(tag => {
            if (skuList.length === 0) {
              if (this.tagName === tag.tagName) {
                let sku = Object.assign({}, this.defalutSku)
                sku.properties = properties // 销售属性组合字符串
                skuList.push(sku)
              } else {
                tag.tagItems.forEach(tagItem => {
                  let sku = Object.assign({}, this.defalutSku)
                  sku.properties = `${tag.tagName}:${tagItem.propValue}` // 销售属性组合字符串
                  skuList.push(sku)
                })
              }
              if (val.length === 1) {
                skuList = this.skuList.concat(skuList)
              }
            } else {
              tempSkuList = []
              if (this.tagName === tag.tagName) {
                skuList.forEach(sku => {
                  if (sku.properties.indexOf(this.tagName) === -1) {
                    let newSku = Object.assign({}, sku)
                    newSku.properties = `${sku.properties};${properties}`
                    tempSkuList.push(newSku)
                  }
                })
              } else {
                tag.tagItems.forEach(tagItem => {
                  skuList.forEach(sku => {
                    if (sku.properties.indexOf(tag.tagName) === -1) {
                      let newSku = Object.assign({}, sku)
                      newSku.properties = `${sku.properties};${tag.tagName}:${tagItem.propValue}`
                      tempSkuList.push(newSku)
                    }
                  })
                })
              }
              skuList = this.skuList.concat(tempSkuList)
              console.log('skuList', skuList)
            }
          })
        } else {
        // 增加或删除规格
          let tempSkuList = []
          val.forEach(tag => {
          // console.log('tag', tag)
            if (skuList.length === 0) {
              tag.tagItems.forEach(tagItem => {
                let sku = Object.assign({}, this.defalutSku)
                sku.properties = `${tag.tagName}:${tagItem.propValue}` // 销售属性组合字符串
                skuList.push(sku)
              })
            } else {
              tempSkuList = []
              tag.tagItems.forEach(tagItem => {
                skuList.forEach(sku => {
                  let newSku = Object.assign({}, sku)
                  newSku.properties = `${sku.properties};${tag.tagName}:${tagItem.propValue}`
                  tempSkuList.push(newSku)
                })
              })
              skuList = tempSkuList
            }
          })
        }
        if (!skuList.length) {
          skuList.push(Object.assign({}, this.defalutSku))
        }
      // debugger
        this.$emit('change', skuList)
      },
      deep: true
    }
  },
  methods: {
    init (skuList) {
      this.value = skuList
      if (!skuList || !skuList.length) {
        this.skuTags = []
        this.$emit('change', [Object.assign({}, this.defalutSku)])
        return
      }
      this.initing = true
      let skuTags = []
      for (let i = 0; i < skuList.length; i++) {
        const sku = skuList[i]
        if (!sku.properties) break
        let propertiesArray = sku.properties.split(';')
        for (let j in propertiesArray) {
          let properties = propertiesArray[j].split(':')
          if (!skuTags[j]) {
            skuTags[j] = {
              tagName: properties[0],
              tagItems: []
            }
            this.tagItemInputs.push({ visible: false, value: '' })
          }
          let tagItemNameIndex = skuTags[j].tagItems.findIndex((tagItemName) => tagItemName.propValue === properties[1])
          if (tagItemNameIndex === -1) {
            // skuTags[j].tagItems.push(properties[1])
            skuTags[j].tagItems.push({propValue: properties[1]})
          }
        }
      }
      this.skuTags = skuTags
    },
    // 显示规格名、规格值输入框
    shopTagInput () {
      this.isShowTagInput = !this.isShowTagInput
    },
    // 隐藏规格名、规格值输入框
    hideTagInput () {
      this.isShowTagInput = false
      this.cleanTagInput()
    },
    addTag () {
      let selectValues = this.addTagInput.selectValues
      if (!this.addTagInput.propName) {
        this.$message.error('请输入规格名')
        return
      }
      if (!selectValues.length) {
        this.$message.error('请输入规格值')
        return
      }
      this.isShowTagInput = false
      for (let i = 0; i < selectValues.length; i++) {
        const element = selectValues[i]
        let is = Object.prototype.toString.call(element) === '[object Object]'
        if (!is) {
          this.maxPropId = this.maxPropId + 1
          break
        }
      }
      let tagItems = []
      for (let i = 0; i < selectValues.length; i++) {
        const element = selectValues[i]
        let is = Object.prototype.toString.call(element) === '[object Object]'
        if (is) {
          tagItems.push(element)
        } else {
          this.maxValueId = this.maxValueId + 1
          tagItems.push({propId: this.maxPropId, propValue: element, valueId: this.maxValueId})
        }
      }
      // 向规格中放入规格输入框内的数据
      this.$store.commit('prod/addSkuTag', {
        tagName: this.addTagInput.propName,
        tagItems
      })
      this.type = 1
      this.cleanTagInput()
    },
    // 清除规格值输入框内容
    cleanTagInput () {
      this.addTagInput = {
        propName: '',
        selectValues: []
      }
      this.dbTagValues = []
    },
      // 规格名输入框，选中规格事件
    handleTagClick () {
        // 清空规格值输入框
      this.dbTagValues = []
      this.addTagInput.selectValues = []
        // 判断规格名输入的值是否为数据库中已有的值
      let specsIndex = this.dbTags.findIndex(spec => spec.propName === this.addTagInput.propName)
        // 如果不是，则说明为用户随便输入
      if (specsIndex === -1) return
        // 如果数据库已有该规格名，则获取根据id获取该规格名称含有的规格值
      this.$http({
        url: this.$http.adornUrl(`/prod/spec/listSpecValue/${this.dbTags[specsIndex].propId}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        this.dbTagValues = data
      })
    },
    // 关闭标签 --删除
    handleTagClose (tagIndex, tagItemIndex) {
      this.tagName = this.skuTags[tagIndex].tagName
      this.tagNameIndex = tagIndex
      this.tagItemName = this.skuTags[tagIndex].tagItems[tagItemIndex].propValue
      if (this.skuTags[tagIndex].tagItems.length === 1) {
        return
      }
      this.type = 4
      this.$store.commit('prod/removeSkuTagItem', { tagIndex, tagItemIndex })
    },
    // 标签输入框确定时调用
    handleInputConfirm (tagIndex) {
      if (this.checkTagItem(tagIndex)) {
        return
      }
      var tagItems = this.skuTags[tagIndex].tagItems
      var itemValue = this.tagItemInputs[tagIndex].value
      let index = tagItems.length - 1
      this.tagName = this.skuTags[tagIndex].tagName
      this.tagItemName = this.tagItemInputs[tagIndex].value
      let maxValue = this.getMaxValueId(this.skuTags[tagIndex].tagItems)
      let tagItem = {propId: index === -1 ? 0 : this.skuTags[tagIndex].tagItems[index].propId, propValue: itemValue, valueId: index === -1 ? 0 : (maxValue + 1)}
      if (tagItem) {
        this.$store.commit('prod/addSkuTagItem', { tagIndex, tagItem })
      }
      this.tagItemInputs[tagIndex].visible = false
      this.tagItemInputs[tagIndex].value = ''
      this.type = 2
    },
    // 显示标签输入框
    showTagInput (tagIndex) {
      this.tagItemInputs.push({ visible: false, value: '' })
      this.tagItemInputs[tagIndex].visible = true
      this.$nextTick(() => {
        this.$refs[`saveTagInput${tagIndex}`][0].$refs.input.focus()
      })
    },
    // 获取数据集合所有对象中某个属性的最大值
    getMaxValueId (list) {
      let value = Math.max.apply(Math, list.map(item => { return item.valueId }))
      return value
    },
    // 删除 规格
    removeTag (tagIndex) {
      this.type = 3
      this.$store.commit('prod/removeSkuTag', tagIndex)
    },
    /**
     * 新增规格值时，判断是否存在同名的规格值
     */
    checkTagItem (tagIndex) {
      let tagItem = this.tagItemInputs[tagIndex].value
      if (!tagItem) {
        this.tagItemInputs[tagIndex].visible = false
        this.tagItemInputs[tagIndex].value = ''
        return true
      }
      var isSame = false
      this.skuTags.forEach(tag => {
        let arr = tag.tagItems.map((item, index) => {
          return item.propValue
        })
        if (arr.indexOf(tagItem) > -1) {
          isSame = true
          this.$message.error('product.specificationValue')
          return false
        }
      })
      return isSame
    }
  }
}
</script>

<style lang="scss">
.mod-prod-sku-tag {
  .el-tag + .el-tag {
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
.sku-border{
  border: 1px solid #EBEEF5;
  width:70%
}
.sku-background{
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
.sku-tag{
  margin: 12px 12px;
}
.tagTree{
  margin-left: 18px;
  padding-bottom:8px;
}
</style>
