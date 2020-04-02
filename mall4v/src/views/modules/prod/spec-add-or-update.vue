<template>
  <el-dialog :title="!this.dataList[0].propId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-table :data="dataList"
              border
              style="width: 100%;">
      <el-table-column prop="propName"
                       header-align="center"
                       align="center"
                       label="属性名称">
        <template slot-scope="scope">
          <el-input placeholder="请输入内容"
                    v-model="scope.row.propName"
                    clearable></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="prodPropValues"
                       header-align="center"
                       align="center"
                       label="属性值">
        <template slot-scope="scope">
          <el-col :span="12"
                  v-for="item in scope.row.prodPropValues"
                  :key="item.valueId">
            <el-input placeholder="请输入内容"
                      v-model="item.propValue"
                      @clear="clearProdPropValues"
                      clearable></el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary"
                       icon="el-icon-circle-plus"
                       @click="addInput()"></el-button>
          </el-col>
        </template>
      </el-table-column>
    </el-table>
    <span slot="footer"
          class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
export default {
  data () {
    return {
      visible: false,
      dataList: [{ propId: 0, propName: '', prodPropValues: [{ valueId: 0 }] }],
      dataRule: {
        propName: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' }
        ]
      },
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      }
    }
  },
  methods: {
    init (val) {
      if (val) {
        this.dataList = [JSON.parse(JSON.stringify(val))]
      } else {
        this.dataList = [
          { propId: 0, propName: '', prodPropValues: [{ valueId: 0 }] }
        ]
      }
      this.visible = true
    },
    // 表单提交
    dataFormSubmit () {
      if (this.dataList[0].prodPropValues) {
        let temp = []
        for (const key in this.dataList[0].prodPropValues) {
          if (this.dataList[0].prodPropValues.hasOwnProperty(key)) {
            const element = this.dataList[0].prodPropValues[key]
            if (element.propValue) {
              temp.push(this.dataList[0].prodPropValues[key])
            }
          }
        }
        this.dataList[0].prodPropValues = temp
      }
      this.$http({
        url: this.$http.adornUrl(`/prod/spec`),
        method: this.dataList[0].propId ? 'put' : 'post',
        data: this.$http.adornData({
          propId: this.dataList[0].propId || undefined,
          propName: this.dataList[0].propName,
          prodPropValues: this.dataList[0].prodPropValues
        })
      }).then(({ data }) => {
        this.$message({
          message: '操作成功',
          type: 'success',
          duration: 1500,
          onClose: () => {
            this.visible = false
            this.$emit('refreshDataList', this.page)
          }
        })
      })
    },
    clearProdPropValues () {
      if (this.dataList[0].prodPropValues.length === 1) {
        return
      }
      for (let i = 0; i < this.dataList[0].prodPropValues.length; i++) {
        const element = this.dataList[0].prodPropValues[i]
        if (!element.propValue) {
          this.dataList[0].prodPropValues.splice(i, 1)
        }
      }
    },
    addInput () {
      let temp = this.dataList[0].prodPropValues
      if (temp[temp.length - 1].propValue) {
        temp.push({})
      }
    }
  }
}
</script>