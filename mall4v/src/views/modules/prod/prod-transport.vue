<template>
  <div class="mod-prod-prod-transport">
    <el-form-item label="运费设置"
                  :rules="[{ required: true, message: '运费模板不能为空'}]">
      <el-select v-model="transportId"
                 placeholder="请选择"
                 @change="changeTransport">
        <el-option v-for="transport in transportList"
                   :key="transport.transportId"
                   :label="transport.transName"
                   :value="transport.transportId">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-table :data="transportInfo.transfees"
                style="width: 100%"
                v-if="transportInfo.transfees">
        <el-table-column label="配送区域"
                         width="350">
          <template slot-scope="scope">
            <span v-if="!scope.row.cityList.length">所有地区</span>
            <el-tag v-for="city in scope.row.cityList"
                    :key="city.areaId"
                    v-else>{{city.areaName}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="firstPiece"
                         :label="tableTitle[0]">
        </el-table-column>
        <el-table-column prop="firstFee"
                         :label="tableTitle[1]">
        </el-table-column>
        <el-table-column prop="continuousPiece"
                         :label="tableTitle[2]">
        </el-table-column>
        <el-table-column prop="continuousFee"
                         :label="tableTitle[3]">
        </el-table-column>
      </el-table>
    </el-form-item>
    <el-form-item v-if="transportInfo.hasFreeCondition === 1">
      <el-table :data="transportInfo.transfeeFrees"
                style="width: 100%">
        <el-table-column label="指定区域"
                         width="350">
          <template slot-scope="scope">
            <el-tag v-for="city in scope.row.freeCityList"
                    :key="city.areaId">{{city.areaName}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="freeType"
                         label="包邮条件">
          <template slot-scope="scope">
            <span v-if="scope.row.freeType === 0">满件/重量/体积包邮</span>
            <span v-if="scope.row.freeType === 1">满金额包邮</span>
            <span v-if="scope.row.freeType === 2">满件/重量/体积且满金额包邮</span>
          </template>
        </el-table-column>
        <el-table-column prop="amount">
          <template slot-scope="scope">
            <span v-if="scope.row.freeType === 1">满{{scope.row.amount}}元金额包邮</span>
            <span v-if="scope.row.freeType === 0">满{{scope.row.piece}}件/重量/体积包邮</span>
            <span v-if="scope.row.freeType === 2">满{{scope.row.piece}}件/重量/体积且满{{scope.row.amount}}元金额包邮</span>
          </template>
        </el-table-column>
      </el-table>
    </el-form-item>
  </div>
</template>

<script>
export default {
  data () {
    return {
      transportId: null,
      transportList: [{
        transportId: null,
        transName: ''
      }],
      transportInfo: {
        hasFreeCondition: false,
        transfeeFrees: [{ freeCityList: [] }]
      }
    }
  },
  props: {
    value: {
      default: null,
      type: Number
    }
  },
  computed: {
    tableTitle () {
      var titles = [['首件(个)', '运费(元)', '续件(个)', '续费(元)'], ['首重(kg)', '运费(元)', '续重(kg)', '续费(元)'], ['首体积(m³)', '运费(元)', '续体积(m³)', '续费(元)']]
      if (this.transportInfo.chargeType) {
        return titles[this.transportInfo.chargeType]
      }
      return titles[0]
    }
  },
  created () {
    this.getTransportList()
  },
  watch: {
    value: function (transportId) {
      this.transportId = transportId
    }
  },
  methods: {
    getTransportList () {
      this.$http({
        url: this.$http.adornUrl('/shop/transport/list'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        this.transportList = data
      })
    },
    changeTransport (transportId) {
      this.$emit('input', transportId)
      if (!transportId) {
        return
      }
      this.$http({
        url: this.$http.adornUrl(`/shop/transport/info/${transportId}`),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        this.transportInfo = data
      })
    }
  }
}
</script>

<style lang="scss">
</style>
