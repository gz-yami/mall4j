<template>
  <div class="shop-transcity-add-or-update">
    <el-dialog
      :modal="false"
      title="选择配送区域"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px" style="height:400px">
        <el-scrollbar style="height:100%">
          <el-form-item size="mini" label="配送区域">
            <el-tree
              :data="menuList"
              :props="menuListTreeProps"
              node-key="areaId"
              ref="menuListTree"
              show-checkbox
              v-if="visible">
            </el-tree>
          </el-form-item>
        </el-scrollbar>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { treeDataTranslate } from '@/utils'
  export default {
    data () {
      return {
        type: 0,
        visible: false,
        menuList: [],
        rowIndex: 0,
        menuListTreeProps: {
          label: 'areaName',
          children: 'children'
        },
        dataForm: {
          transfeeId: 0
        }
      }
    },
    methods: {
      init (rowIndex, cityList, allSelectCityList, type) {
        this.type = type
        this.rowIndex = rowIndex
        if (this.menuList.length === 0) {
          // 获取所有省和市
          this.$http({
            url: this.$http.adornUrl('/admin/area/list'),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            this.menuList = treeDataTranslate(data, 'areaId', 'parentId')
          }).then(() => {
            this.visible = true
            this.disabledNodes(cityList, allSelectCityList)
            this.$nextTick(() => {
              this.$refs['dataForm'].resetFields()
              this.$refs.menuListTree.setCheckedNodes(cityList)
            })
          })
        } else {
          this.visible = true
          this.disabledNodes(cityList, allSelectCityList)
          this.$nextTick(() => {
            this.$refs['dataForm'].resetFields()
            this.$refs.menuListTree.setCheckedNodes(cityList)
          })
        }
      },
      disabledNodes (cityList, allSelectCityList) {
        for (let i = 0; i < this.menuList.length; i++) {
          const childrens = this.menuList[i].children
          this.menuList[i].disabled = false
          let disabledNum = 0
          for (let j = 0; j < childrens.length; j++) {
            const city = childrens[j]
            this.menuList[i].children[j].disabled = false
            let allHasCity = allSelectCityList.findIndex((item) => city.areaId === item.areaId) > -1
            let listHasCity = cityList.findIndex((item) => city.areaId === item.areaId) > -1
            if (allHasCity && !listHasCity) {
              this.menuList[i].children[j].disabled = true
              disabledNum++
            }
          }
          if (disabledNum === this.menuList[i].children.length) {
            this.menuList[i].disabled = true
          }
        }
      },
      // 表单提交
      dataFormSubmit () {
        this.$emit('refreshDataList', this.rowIndex, this.$refs.menuListTree.getCheckedNodes(true), this.type)
        this.visible = false
      }
    }
  }
</script>

<style  lang="scss">
.shop-transcity-add-or-update {
  .el-scrollbar__wrap{overflow-x:hidden;}
}
</style>
