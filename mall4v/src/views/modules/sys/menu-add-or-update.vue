<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="dataForm.type">
          <el-radio v-for="(type, index) in dataForm.typeList" :label="index" :key="index">{{ type }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="dataForm.typeList[dataForm.type] + '名称'" prop="name">
        <el-input v-model="dataForm.name" :placeholder="dataForm.typeList[dataForm.type] + '名称'"></el-input>
      </el-form-item>
      <el-form-item label="上级菜单">
        <el-cascader
          expand-trigger="hover"
          :options="menuList"
          :props="menuListTreeProps"
          change-on-select
          v-model="selectedMenu"
          @change="handleSelectMenuChange">
        </el-cascader>
      </el-form-item>
      <el-form-item v-if="dataForm.type === 1" label="菜单路由" prop="url">
        <el-input v-model="dataForm.url" placeholder="菜单路由"></el-input>
      </el-form-item>
      <el-form-item v-if="dataForm.type !== 0" label="授权标识" prop="perms">
        <el-input v-model="dataForm.perms" placeholder="多个用逗号分隔, 如: user:list,user:create"></el-input>
      </el-form-item>
      <el-form-item v-if="dataForm.type !== 2" label="排序号" prop="orderNum">
        <el-input-number v-model="dataForm.orderNum" controls-position="right" :min="0" label="排序号"></el-input-number>
      </el-form-item>
      <el-form-item v-if="dataForm.type !== 2" label="菜单图标" prop="icon">
        <el-row>
          <el-col :span="22">
            <el-popover
              ref="iconListPopover"
              placement="bottom-start"
              trigger="click"
              popper-class="mod-menu__icon-popover">
              <div class="mod-menu__icon-list">
                <el-button
                  v-for="(item, index) in iconList"
                  :key="index"
                  @click="iconActiveHandle(item)"
                  :class="{ 'is-active': item === dataForm.icon }">
                  <icon-svg :name="item"></icon-svg>
                </el-button>
              </div>
            </el-popover>
            <el-input v-model="dataForm.icon" v-popover:iconListPopover :readonly="true" placeholder="菜单图标名称" class="icon-list__input"></el-input>
          </el-col>
          <el-col :span="2" class="icon-list__tips">
            <el-tooltip placement="top" effect="light">
              <div slot="content">全站推荐使用SVG Sprite, 详细请参考:icons/index.js 描述</div>
              <i class="el-icon-warning"></i>
            </el-tooltip>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { treeDataTranslate, idList } from '@/utils'
  import Icon from '@/icons'
  import { Debounce } from '@/utils/debounce'
  export default {
    data () {
      var validateUrl = (rule, value, callback) => {
        if (this.dataForm.type === 1 && !/\S/.test(value)) {
          callback(new Error('菜单URL不能为空'))
        } else {
          callback()
        }
      }
      return {
        visible: false,
        dataForm: {
          id: 0,
          type: 1,
          typeList: ['目录', '菜单', '按钮'],
          name: '',
          parentId: 0,
          url: '',
          perms: '',
          orderNum: 0,
          icon: '',
          iconList: []
        },
        dataRule: {
          name: [
            { required: true, message: '菜单名称不能为空', trigger: 'blur' },
            { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的菜单名称', trigger: 'blur' }
          ],
          url: [
            { validator: validateUrl, trigger: 'blur' }
          ]
        },
        menuList: [],
        selectedMenu: [],
        menuListTreeProps: {
          value: 'menuId',
          label: 'name'
        }
      }
    },
    created () {
      this.iconList = Icon.getNameList()
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.$http({
          url: this.$http.adornUrl('/sys/menu/list'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.menuList = treeDataTranslate(data, 'menuId')
        }).then(() => {
          this.visible = true
          this.$nextTick(() => {
            this.$refs['dataForm'].resetFields()
          })
        }).then(() => {
          if (this.dataForm.id) {
            // 修改
            this.$http({
              url: this.$http.adornUrl(`/sys/menu/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              this.dataForm.id = data.menuId
              this.dataForm.type = data.type
              this.dataForm.name = data.name
              this.dataForm.parentId = data.parentId
              this.dataForm.url = data.url
              this.dataForm.perms = data.perms
              this.dataForm.orderNum = data.orderNum
              this.dataForm.icon = data.icon
              this.selectedMenu = idList(this.menuList, data.parentId, 'menuId', 'children').reverse()
            })
          } else {
            this.selectedMenu = []
          }
        })
      },
      handleSelectMenuChange (val) {
        this.dataForm.parentId = val[val.length - 1]
      },
      // 图标选中
      iconActiveHandle (iconName) {
        this.dataForm.icon = iconName
      },
      // 表单提交
      dataFormSubmit: Debounce(function () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/sys/menu`),
              method: this.dataForm.id ? 'put' : 'post',
              data: this.$http.adornData({
                'menuId': this.dataForm.id || undefined,
                'type': this.dataForm.type,
                'name': this.dataForm.name,
                'parentId': this.dataForm.parentId,
                'url': this.dataForm.url,
                'perms': this.dataForm.perms,
                'orderNum': this.dataForm.orderNum,
                'icon': this.dataForm.icon
              })
            }).then(({data}) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            })
          }
        })
      })
    }
  }
</script>

<style lang="scss">

  .mod-menu {
    .menu-list__input,
    .icon-list__input {
       > .el-input__inner {
        cursor: pointer;
      }
    }
    &__icon-popover {
      max-width: 420px;
    }
    &__icon-list {
      max-height: 180px;
      padding: 0;
      margin: -8px 0 0 -8px;
      > .el-button {
        padding: 8px;
        margin: 8px 0 0 8px;
        > span {
          display: inline-block;
          vertical-align: middle;
          width: 18px;
          height: 18px;
          font-size: 18px;
        }
      }
    }
    .icon-list__tips {
      font-size: 18px;
      text-align: center;
      color: #e6a23c;
      cursor: pointer;
    }
  }
</style>
