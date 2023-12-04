<template>
  <el-dialog
    v-model="visible"
    :title="!dataForm.addrId ? '新增' : '修改'"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="80px"
      @keyup.enter="onSubmit()"
    >
      <el-form-item
        label="名称"
        prop="addrName"
      >
        <el-input
          v-model="dataForm.addrName"
          placeholder="自提点名称"
        />
      </el-form-item>
      <el-form-item label="省份">
        <el-col :span="8">
          <el-form-item prop="province">
            <el-select
              v-model="dataForm.provinceId"
              placeholder="请选择"
              @change="selectProvince"
            >
              <el-option
                v-for="province in provinceList"
                :key="province.areaId"
                :label="province.areaName"
                :value="province.areaId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="city">
            <el-select
              v-model="dataForm.cityId"
              placeholder="请选择"
              @change="selectCity"
            >
              <el-option
                v-for="city in cityList"
                :key="city.areaId"
                :label="city.areaName"
                :value="city.areaId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="area">
            <el-select
              v-model="dataForm.areaId"
              placeholder="请选择"
            >
              <el-option
                v-for="area in areaList"
                :key="area.areaId"
                :label="area.areaName"
                :value="area.areaId"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item
        label="地址"
        prop="addr"
      >
        <el-input
          v-model="dataForm.addr"
          placeholder="地址"
        />
      </el-form-item>
      <el-form-item
        label="手机号"
        prop="mobile"
      >
        <el-input
          v-model="dataForm.mobile"
          maxlength="11"
          placeholder="手机号"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span
        class="dialog-footer"
      >
        <el-button @click="visible = false">取消</el-button>
        <el-button
          type="primary"
          @click="onSubmit()"
        >确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { isMobile } from '@/utils/validate'
import { Debounce } from '@/utils/debounce'
const emit = defineEmits(['refreshDataList'])
const visible = ref(false)
// eslint-disable-next-line no-unused-vars
const validateMobile = (rule, value, callback) => {
  if (!isMobile(value)) {
    callback(new Error('手机号格式错误'))
  } else {
    callback()
  }
}
const dataRule = {
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
const provinceList = ref([])
const dataFormRef = ref(null)
const cityList = ref([])
const areaList = ref([])
const dataForm = reactive({
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
})
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
const init = (id) => {
  dataForm.addrId = id || 0
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
    cityList.value = []
    areaList.value = []
    dataForm.provinceId = null
    dataForm.cityId = null
    dataForm.areaId = null
  })
  listAreaByParentId().then(({ data }) => {
    provinceList.value = data
  })
  if (dataForm.addrId) {
    http({
      url: http.adornUrl(
        `/shop/pickAddr/info/${dataForm.addrId}`
      ),
      method: 'get',
      params: http.adornParams()
    })
      .then(({ data }) => {
        dataForm.addr = data.addr
        dataForm.mobile = data.mobile
        dataForm.addrName = data.addrName
        dataForm.areaId = data.areaId
        dataForm.cityId = data.cityId
        dataForm.provinceId = data.provinceId
        listAreaByParentId(data.provinceId).then(({ data }) => {
          cityList.value = data
        })
        listAreaByParentId(data.cityId).then(({ data }) => {
          areaList.value = data
        })
      })
  }
}
defineExpose({ init })

const listAreaByParentId = (pid) => {
  if (!pid) pid = 0
  return http({
    url: http.adornUrl('/admin/area/listByPid'),
    method: 'get',
    params: http.adornParams({ pid })
  })
}

/**
 * 选择省
 * @param val
 */
const selectProvince = (val) => {
  dataForm.cityId = null
  dataForm.city = ''
  // 获取城市的select
  listAreaByParentId(val).then(({ data }) => {
    cityList.value = data
  })
}

/**
 * 选择市
 * @param val
 */
const selectCity = (val) => {
  dataForm.areaId = null
  dataForm.area = ''
  // 获取区的select
  listAreaByParentId(val).then(({ data }) => {
    areaList.value = data
  })
}

/**
 * 表单提交
 */
const onSubmit = Debounce(() => {
  for (let i = 0; i < provinceList.value.length; i++) {
    if (provinceList.value[i].areaId === dataForm.provinceId) {
      // 将省名字保存起来
      dataForm.province = provinceList.value[i].areaName
    }
  }
  for (let i = 0; i < cityList.value.length; i++) {
    if (cityList.value[i].areaId === dataForm.cityId) {
      // 将市名字保存起来
      dataForm.city = cityList.value[i].areaName
    }
  }
  for (let i = 0; i < areaList.value.length; i++) {
    if (areaList.value[i].areaId === dataForm.areaId) {
      // 将市名字保存起来
      dataForm.area = areaList.value[i].areaName
    }
  }
  dataFormRef.value?.validate(valid => {
    if (valid) {
      http({
        url: http.adornUrl('/shop/pickAddr'),
        method: dataForm.addrId ? 'put' : 'post',
        data: http.adornData({
          addrId: dataForm.addrId || undefined,
          addr: dataForm.addr,
          addrName: dataForm.addrName,
          mobile: dataForm.mobile,
          area: dataForm.area,
          city: dataForm.city,
          province: dataForm.province,
          areaId: dataForm.areaId,
          cityId: dataForm.cityId,
          provinceId: dataForm.provinceId
        })
      })
        .then(() => {
          ElMessage({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              visible.value = false
              emit('refreshDataList', page)
            }
          })
        })
    }
  })
})
</script>
