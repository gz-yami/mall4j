<template>
  <el-dialog :title="!dataForm.addrId ? '新增' : '修改'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="名称"
                    prop="addrName">
        <el-input v-model="dataForm.addrName"
                  placeholder="自提点名称"></el-input>
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
      <el-form-item label="地址"
                    prop="addr">
        <el-input v-model="dataForm.addr"
                  placeholder="地址"></el-input>
      </el-form-item>
      <el-form-item label="手机号"
                    prop="mobile">
        <el-input v-model="dataForm.mobile"
                  placeholder="手机号"></el-input>
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
import { isMobile } from '@/utils/validate'
import { Debounce } from '@/utils/debounce'

export default {
  data () {
    var validateMobile = (rule, value, callback) => {
      if (!isMobile(value)) {
        callback(new Error('手机号格式错误'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      provinceList: [],
      cityList: [],
      areaList: [],
      dataForm: {
        addrId: 0,
        addr: '',
        addrName: '',
        mobile: '',
        area: '',
        city: '',
        province: '',
        areaId: null,
        cityId: null,
        provinceId: null
      },
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      },
      dataRule: {
        addrName: [
          { required: true, message: '自提点名称不能为空', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的自提点名称', trigger: 'blur' }
        ],
        addr: [
          { required: true, message: '地址不能为空', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的地址', trigger: 'blur' }
        ],
        city: [{ required: true, message: '城市不能为空', trigger: 'blur' }],
        province: [
          { required: true, message: '省份不能为空', trigger: 'blur' }
        ],
        area: [{ required: true, message: '区/县不能为空', trigger: 'blur' }],
        mobile: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { validator: validateMobile, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.addrId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields()
        this.cityList = []
        this.areaList = []
        this.dataForm.provinceId = null
        this.dataForm.cityId = null
        this.dataForm.areaId = null
      })
      this.listAreaByParentId().then(({ data }) => {
        this.provinceList = data
      })
      if (this.dataForm.addrId) {
        this.$http({
          url: this.$http.adornUrl(
            `/shop/pickAddr/info/${this.dataForm.addrId}`
          ),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.dataForm.addr = data.addr
          this.dataForm.mobile = data.mobile
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
    dataFormSubmit: Debounce(function () {
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
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/shop/pickAddr`),
            method: this.dataForm.addrId ? 'put' : 'post',
            data: this.$http.adornData({
              addrId: this.dataForm.addrId || undefined,
              addr: this.dataForm.addr,
              addrName: this.dataForm.addrName,
              mobile: this.dataForm.mobile,
              area: this.dataForm.area,
              city: this.dataForm.city,
              province: this.dataForm.province,
              areaId: this.dataForm.areaId,
              cityId: this.dataForm.cityId,
              provinceId: this.dataForm.provinceId
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
        }
      })
    })
  }
}
</script>
