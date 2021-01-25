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
          :key="tagItem"
          closable
          :disable-transitions="false"
          @close="handleTagClose(tagIndex, tagItemIndex)">
          {{tagItem}}
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
        tagItemInputs: [],
        // sku的标记
        // tags: [],
        // 数据库中的规格
        dbTags: [],
        // 根据选定的规格所查询出来的规格值
        dbTagValues: [],
        specs: [], // 使用的规格
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
      })
    },
    // props: {
    //   tags: { // sku的标记
    //     default: [],
    //     type: Array
    //   }
    // },
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
          let tempSkuList = []
          val.forEach(tag => {
            if (skuList.length === 0) {
              tag.tagItems.forEach(tagItem => {
                let sku = Object.assign({}, this.defalutSku)
                sku.properties = `${tag.tagName}:${tagItem}` // 销售属性组合字符串
                skuList.push(sku)
              })
            } else {
              tempSkuList = []
              tag.tagItems.forEach(tagItem => {
                skuList.forEach(sku => {
                  let newSku = Object.assign({}, sku)
                  newSku.properties = `${sku.properties};${tag.tagName}:${tagItem}`
                  tempSkuList.push(newSku)
                })
              })
              skuList = tempSkuList
            }
          })
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
            let cnProperties = propertiesArray[j].split(':')
            if (!skuTags[j]) {
              skuTags[j] = {
                tagName: cnProperties[0],
                tagItems: []
              }
              this.tagItemInputs.push({visible: false, value: ''})
            }
            let tagItemNameIndex = skuTags[j].tagItems.findIndex((tagItemName) => tagItemName === cnProperties[1])
            if (tagItemNameIndex === -1) {
              skuTags[j].tagItems.push(cnProperties[1])
            }
          }
        }
        this.skuTags = skuTags
      },
      // 显示规格名、规格值输入框
      shopTagInput () {
        this.isShowTagInput = true
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
        let tagItems = []
        for (let i = 0; i < selectValues.length; i++) {
          const element = selectValues[i]
          tagItems.push(element)
        }
        // 向规格中放入规格输入框内的数据
        this.$store.commit('prod/addSkuTag', {
          tagName: this.addTagInput.propName,
          tagItems
        })
        this.cleanTagInput()
      },
      // 清除规格值输入框内容
      cleanTagInput () {
        this.addTagInput = {
          propName: '',
          selectValues: []
        }
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
      // 关闭标签
      handleTagClose (tagIndex, tagItemIndex) {
        if (this.skuTags[tagIndex].tagItems.length === 1) {
          return
        }
        this.$store.commit('prod/removeSkuTagItem', {tagIndex, tagItemIndex})
      },
      // 标签输入框确定时调用
      handleInputConfirm (tagIndex) {
        let tagItem = this.tagItemInputs[tagIndex].value
        if (tagItem) {
          this.$store.commit('prod/addSkuTagItem', {tagIndex, tagItem})
        }
        this.tagItemInputs[tagIndex].visible = false
        this.tagItemInputs[tagIndex].value = ''
      },
      // 显示标签输入框
      showTagInput (tagIndex) {
        this.tagItemInputs.push({visible: false, value: ''})
        this.tagItemInputs[tagIndex].visible = true
        this.$nextTick(() => {
          this.$refs[`saveTagInput${tagIndex}`][0].$refs.input.focus()
        })
      },
      removeTag (tagIndex) {
        this.$store.commit('prod/removeSkuTag', tagIndex)
      }
    }
  }
</script>

<style lang="scss">

  .mod-prod-sku-tag{
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
</style>
