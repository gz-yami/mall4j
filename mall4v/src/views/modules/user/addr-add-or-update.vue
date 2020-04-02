<template>
  <el-dialog :title="!dataForm.addrId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="收货人姓名"
                    prop="receiver">
        <el-input v-model="dataForm.receiver"></el-input>
      </el-form-item>
      <el-form-item label="省份">
        <el-col :span="8">
          <el-form-item prop="province">
            <el-select v-model="dataForm.provinceId"
                       placeholder="请选择"
                       @change="selectProvince">
              <el-option v-for="province in provinceList"
                         :key="province.areaId"
                         :label="province.areaName"
                         :value="province.areaId"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="city">
            <el-select v-model="dataForm.cityId"
                       placeholder="请选择"
                       @change="selectCity">
              <el-option v-for="city in cityList"
                         :key="city.areaId"
                         :label="city.areaName"
                         :value="city.areaId"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="area">
            <el-select v-model="dataForm.areaId"
                       placeholder="请选择">
              <el-option v-for="area in areaList"
                         :key="area.areaId"
                         :label="area.areaName"
                         :value="area.areaId"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item label="详细地址"
                    prop="addr">
        <el-input v-model="dataForm.addr"></el-input>
      </el-form-item>
      <el-form-item label="邮编"
                    prop="postCode">
        <el-input v-model="dataForm.postCode"></el-input>
      </el-form-item>
      <el-form-item label="手机"
                    prop="mobile">
        <el-input v-model="dataForm.mobile"></el-input>
      </el-form-item>
      <el-form-item label="状态"
                    prop="status">
        <el-radio-group v-model="dataForm.status"
                        size="medium">
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="默认地址"
                    prop="commonAddr">
        <el-radio-group v-model="dataForm.commonAddr"
                        size="medium">
          <el-radio :label="0">否</el-radio>
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
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
      roleList: [],
      provinceList: [],
      cityList: [],
      areaList: [],
      dataForm: {
        addrId: null,
        userId: null,
        receiver: null,
        provinceId: null,
        province: null,
        city: null,
        cityId: null,
        area: null,
        areaId: null,
        postCode: null,
        addr: null,
        mobile: null,
        status: 1,
        commonAddr: 0
      },
      dataRule: {
      }
    }
  },
  methods: {
    init (addrId) {
      this.dataForm.addrId = addrId || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.cityList = []
        this.areaList = []
        this.dataForm.provinceId = null
        this.dataForm.cityId = null
        this.dataForm.areaId = null
        if (this.dataForm.addrId) {
          this.$http({
            url: this.$http.adornUrl(`/user/addr/info/${this.dataForm.addrId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            this.dataForm = data
            this.dataForm.addr = data.addr
            this.dataForm.addrName = data.addrName
            this.dataForm.areaId = data.areaId
            this.dataForm.cityId = data.cityId
            this.dataForm.provinceId = data.provinceId
            this.listAreaByParentId(data.provinceId).then(({ data }) => {
              this.cityList = data
            })
            this.listAreaByParentId(data.cityId).then(({ data }) => {
              this.areaList = data
            })
          })
        }
      })
      this.listAreaByParentId().then(({ data }) => {
        this.provinceList = data
      })
    },
    listAreaByParentId (pid) {
      if (!pid) pid = 0
      return this.$http({
        url: this.$http.adornUrl(`/admin/area/listByPid`),
        method: 'get',
        params: this.$http.adornParams({ pid })
      })
    },
    // 选择省
    selectProvince (val) {
      this.dataForm.cityId = null
      this.dataForm.city = ''
      // 获取城市的select
      this.listAreaByParentId(val).then(({ data }) => {
        this.cityList = data
      })
    },
    // 选择市
    selectCity (val) {
      this.dataForm.areaId = null
      this.dataForm.area = ''
      // 获取区的select
      this.listAreaByParentId(val).then(({ data }) => {
        this.areaList = data
      })
    },

    // 表单提交
    dataFormSubmit () {
      for (let i = 0; i < this.provinceList.length; i++) {
        if (this.provinceList[i].areaId === this.dataForm.provinceId) {
          // 将省名字保存起来
          this.dataForm.province = this.provinceList[i].areaName
        }
      }
      for (let i = 0; i < this.cityList.length; i++) {
        if (this.cityList[i].areaId === this.dataForm.cityId) {
          // 将市名字保存起来
          this.dataForm.city = this.cityList[i].areaName
        }
      }
      for (let i = 0; i < this.areaList.length; i++) {
        if (this.areaList[i].areaId === this.dataForm.areaId) {
          // 将市名字保存起来
          this.dataForm.area = this.areaList[i].areaName
        }
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/user/addr'),
            method: this.dataForm.addrId ? 'put' : 'post',
            data: this.$http.adornData(this.dataForm)
          }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.visible = false
                this.$refs['dataForm'].resetFields()
                this.$emit('refreshDataList')
              }
            })
          })
        }
      })
    }
  }
}
</script>
