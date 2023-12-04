<template>
  <div class="mod-prod-prod-transport">
    <el-form-item
      label="运费设置"
      :rules="[{ required: true, message: '运费模板不能为空'}]"
    >
      <el-select
        v-model="transportId"
        placeholder="请选择"
        @change="changeTransport"
      >
        <el-option
          v-for="transport in transportList"
          :key="transport.transportId"
          :label="transport.transName"
          :value="transport.transportId"
        />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-table
        v-if="transportInfo.transfees"
        :data="transportInfo.transfees"
        style="width: 100%"
      >
        <el-table-column
          label="配送区域"
          width="350"
        >
          <template #default="scope">
            <span v-if="!scope.row.cityList.length">所有地区</span>
            <el-tag
              v-for="city in scope.row.cityList"
              v-else
              :key="city.areaId"
            >
              {{ city.areaName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="firstPiece"
          :label="tableTitle[0]"
        />
        <el-table-column
          prop="firstFee"
          :label="tableTitle[1]"
        />
        <el-table-column
          prop="continuousPiece"
          :label="tableTitle[2]"
        />
        <el-table-column
          prop="continuousFee"
          :label="tableTitle[3]"
        />
      </el-table>
    </el-form-item>
    <el-form-item v-if="transportInfo.hasFreeCondition === 1">
      <el-table
        :data="transportInfo.transfeeFrees"
        style="width: 100%"
      >
        <el-table-column
          label="指定区域"
          width="350"
        >
          <template #default="scope">
            <el-tag
              v-for="city in scope.row.freeCityList"
              :key="city.areaId"
            >
              {{ city.areaName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="freeType"
          label="包邮条件"
        >
          <template #default="scope">
            <span v-if="scope.row.freeType === 0">满件/重量/体积包邮</span>
            <span v-if="scope.row.freeType === 1">满金额包邮</span>
            <span v-if="scope.row.freeType === 2">满件/重量/体积且满金额包邮</span>
          </template>
        </el-table-column>
        <el-table-column prop="amount">
          <template #default="scope">
            <span v-if="scope.row.freeType === 1">满{{ scope.row.amount }}元金额包邮</span>
            <span v-if="scope.row.freeType === 0">满{{ scope.row.piece }}件/重量/体积包邮</span>
            <span v-if="scope.row.freeType === 2">满{{ scope.row.piece }}件/重量/体积且满{{ scope.row.amount }}元金额包邮</span>
          </template>
        </el-table-column>
      </el-table>
    </el-form-item>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    default: null,
    type: Number
  }
})
const emit = defineEmits(['input', 'update:modelValue'])
const transportId = ref(null)
const tableTitle = computed(() => {
  const titles = [['首件(个)', '运费(元)', '续件(个)', '续费(元)'], ['首重(kg)', '运费(元)', '续重(kg)', '续费(元)'], ['首体积(m³)', '运费(元)', '续体积(m³)', '续费(元)']]
  if (transportInfo.value.chargeType) {
    return titles[transportInfo.value.chargeType]
  }
  return titles[0]
})

watch(
  () => props.modelValue,
  (id) => {
    transportId.value = id
  })

onMounted(() => {
  getTransportList()
})

const transportList = ref([{
  transportId: null,
  transName: ''
}])
const getTransportList = () => {
  http({
    url: http.adornUrl('/shop/transport/list'),
    method: 'get',
    params: http.adornParams({})
  })
    .then(({ data }) => {
      transportList.value = data
    })
}
const transportInfo = ref({
  hasFreeCondition: false,
  transfeeFrees: [{ freeCityList: [] }]
})
const changeTransport = (id) => {
  emit('update:modelValue', id)
  if (!id) {
    return
  }
  http({
    url: http.adornUrl(`/shop/transport/info/${transportId.value}`),
    method: 'get',
    params: http.adornParams({})
  })
    .then(({ data }) => {
      transportInfo.value = data
    })
}

</script>
