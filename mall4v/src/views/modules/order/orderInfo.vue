<template>
  <el-dialog :title="!dataForm.orderNumber ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible" width="80%">
    <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <div class="main">
        <div class="content">
          <div class="order-number">
            <div class="num-cont">
              <el-form-item label="订单编号:">
                <span class="text">{{dataForm.orderNumber}}</span>
              </el-form-item>
              <el-form-item>
                <el-steps :active="stepsStatus" align-center :process-status="dataForm.status == 6 ? 'error':'wait'">
                  <el-step title="提交订单" :description="dataForm.orderTime"></el-step>
                  <el-step title="买家已付款" :description="dataForm.payTime"></el-step>
                  <el-step title="卖家已发货" v-if="dataForm.orderType !== 1" :description="dataForm.dvyTime"></el-step>
                  <el-step title="买家已收货" v-if="dataForm.orderType !== 1" :description="dataForm.finallyTime"></el-step>
                </el-steps>
              </el-form-item>
            </div>
          </div>
          <div class="order-state">
            <div class="state-cont">
              <div class="state-title">
                <el-form-item label="订单状态:">
                  <template slot-scope="scope">
                    <el-tag v-if="dataForm.status === 1" size="small" type="warning">待付款</el-tag>
                    <el-tag v-if="dataForm.status === 2 && dataForm.orderType !== 1" size="small" type="warning">待发货</el-tag>
                    <el-tag v-if="dataForm.status === 3 && dataForm.orderType !== 1" size="small" type="warning">待收货</el-tag>
                    <el-tag v-if="dataForm.status === 4 && dataForm.orderType !== 1" size="small" type="warning">待评价</el-tag>
                    <el-tag v-if="dataForm.status === 5" size="small" type="success">成功</el-tag>
                    <el-tag v-if="dataForm.status === 6" size="small" type="danger">失败</el-tag>
                  </template>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-button type="primary" v-if="dataForm.status === 2 && dataForm.orderType !== 1" plain @click="changeOrder(dataForm.orderNumber)">发货</el-button>
                    <!-- <el-button type="info" plain>打印</el-button> -->
                  </el-row>
                </el-form-item>
              </div>
              <div class="order-info">
                <div class="order-details">
                  <div class="detail-title">
                    <img src="~@/assets/img/car.png" alt="">
                    <span class="prompt">买家付款后才可以发货</span>
                  </div>
                  <div class="detail-cont">
                    <div class="detail01">
                      <img src="~@/assets/img/address.png" alt="">
                      <div class="text-width">
                        <!-- <span class="revise-addr" @click="changeAddr(dataForm.addrOrderId)">
                          <img src="~@/assets/img/revise.png" alt=""/>修改地址
                        </span> -->
                        <el-form-item label="收货人:">
                          <span>{{dataForm.userAddrOrder.receiver}}</span>
                        </el-form-item>
                        <el-form-item label="手机:">
                          <span>{{dataForm.userAddrOrder.mobile}}</span>
                        </el-form-item>
                        <el-form-item label="收货地址">
                          <span>{{dataForm.userAddrOrder.province}}{{dataForm.userAddrOrder.city}}{{dataForm.userAddrOrder.area}}{{dataForm.userAddrOrder.addr}}</span>
                        </el-form-item>
                      </div>
                    </div>
                    <div class="detail01">
                      <img src="~@/assets/img/invoice.png" alt="">
                      <span>不开发票</span>
                    </div>
                  </div>
                </div>
                <div class="buyers">
                  <div class="detail-title">
                    <img src="~@/assets/img/buyer.png" alt="" style="margin-right:15px">
                    <el-form-item label="买家:" style="margin-top:22px" label-width="44px">
                      <span>{{dataForm.nickName}}</span>
                    </el-form-item>
                  </div>
                  <div class="buyers-info">
                    <div class="detail02">
                      <img src="~@/assets/img/message.png" alt="">
                      <div class="text-width">
                        <span>买家备注:</span>
                        <br>
                        <span>{{dataForm.remarks}}</span>
                      </div>
                    </div>
                    <!-- <div class="detail02">
                      <img src="~@/assets/img/remarks.png" alt="" width="18">
                      <div class="text-width">
                        <span>卖家备注：</span><br>
                        <span>给我加多点香菜</span>
                      </div>
                    </div> -->
                  </div>
                </div>
              </div>
              <div class="item-list">
                <el-table :data="dataForm.orderItems" border>
                  <el-table-column prop="" label="商品">
                    <template slot-scope="scope">
                      <img :src="resourcesUrl + scope.row.pic" width="100" height="100"/>
                      <span>{{scope.row.prodName}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="price" label="单价" width="180" align="center">
                    <template slot-scope="scope">
                      <span>{{scope.row.price}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="count" label="数量" width="180" align="center">
                    <template slot-scope="scope">
                      <span>{{scope.row.prodCount}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="totalPrice" label="总价" width="180" align="center">
                    <template slot-scope="scope">
                      <span>{{scope.row.productTotalAmout}}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div class="item-info">
                <el-form-item label="商品总价:">
                  <span class="text">￥{{dataForm.total}}</span>
                </el-form-item>
                <!-- <el-form-item label="店铺优惠:">
                <span class="text">￥15.00</span>
                </el-form-item> -->
                <!-- <el-form-item label="积分抵扣:">
                <span class="text">￥15.00</span>
                </el-form-item> -->
                <!-- <el-form-item label="发票费用:">
                <span class="text">￥15.00</span>
                </el-form-item> -->
                <el-form-item label="配送费用:" v-if="dataForm.freightAmount">
                  <span class="text">￥{{dataForm.freightAmount}}</span>
                </el-form-item>
                <el-form-item label="应付金额:">
                  <span class="text">￥{{dataForm.actualTotal}}</span>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="order-log">
            <div class="log-title">
              <span>订单日志</span>
            </div>
            <div class="log-cont">
              <el-form-item v-if="dataForm.orderTime" label-width="10px">
                <span>{{dataForm.orderTime}} {{dataForm.nickName}} 创建订单（成功）</span>
              </el-form-item>
              <el-form-item v-if="dataForm.updateTime" label-width="10px">
                <span>{{dataForm.updateTime}} {{dataForm.nickName}} 订单更新（成功）</span>
              </el-form-item>
              <el-form-item v-if="dataForm.payTime" label-width="10px">
                <span>{{dataForm.payTime}} {{dataForm.nickName}} 订单付款（成功）</span>
              </el-form-item>
              <el-form-item v-if="dataForm.dvyTime" label-width="10px">
                <span>{{dataForm.dvyTime}} {{dataForm.nickName}} 订单发货（成功）</span>
              </el-form-item>
              <el-form-item v-if="dataForm.finallyTime" label-width="10px">
                <span>{{dataForm.finallyTime}} {{dataForm.nickName}} 完成订单（成功）</span>
              </el-form-item>
              <el-form-item v-if="dataForm.cancelTime" label-width="10px">
                <span>{{dataForm.cancelTime}} {{dataForm.nickName}} 取消订单（成功）</span>
              </el-form-item>
            </div>
          </div>
        </div>
      </div>
    </el-form>
    <!-- 弹窗, 新增 / 修改 -->
    <!-- <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update> -->
    <devy-add v-if="devyVisible" ref="devyAdd"  @refreshDataList="getDataList"></devy-add>
  </el-dialog>
</template>

<script>
  // import AddOrUpdate from './order-addr'
  import DevyAdd from './order-devy'
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          orderId: 0,
          orderNumber: '',
          remarks: '',
          total: 0,
          actualTotal: 0,
          dvyType: '',
          status: 1,
          addrOrderId: 0,
          nickName: '',
          orderItems: [],
          orderTime: '',
          updateTime: '',
          payTime: '',
          dvyTime: '',
          finallyTime: '',
          cancelTime: '',
          userAddrOrder: {}
        },
        resourcesUrl: window.SITE_CONFIG.resourcesUrl,
        addOrUpdateVisible: false,
        devyVisible: false
      }
    },
    components: {
      // AddOrUpdate,
      DevyAdd
    },
    watch: {
      visible: function () {
        if (!this.visible) {
          this.devyVisible = false
          this.addOrUpdateVisible = false
        }
      }
    },
    computed: {
      stepsStatus: function () {
        if (this.dataForm.finallyTime) {
          return 4
        }
        if (this.dataForm.dvyTime) {
          return 3
        }
        if (this.dataForm.payTime) {
          return 2
        }
        if (this.dataForm.orderTime) {
          return 1
        }
      }
    },
    methods: {
      init (orderNumber) {
        this.dataForm.orderNumber = orderNumber || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
        })
        if (this.dataForm.orderNumber) {
          // 修改
          this.$http({
            url: this.$http.adornUrl(`/order/order/orderInfo/${this.dataForm.orderNumber}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            this.dataForm = data
          })
        }
      },
      getDataList () {
        this.$http({
          url: this.$http.adornUrl(`/order/order/orderInfo/${this.dataForm.orderNumber}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.dataForm = data
        })
      },
      // 表单提交
      dataFormSubmit () {
      },
      // 发货
      changeOrder (orderNumber) {
        this.devyVisible = true
        this.$nextTick(() => {
          this.$refs.devyAdd.init(orderNumber, this.dataForm.dvyId, this.dataForm.dvyFlowId)
        })
      },
      // 修改备注
      changeRemarks () {
      },
      // 修改地址
      changeAddr (val) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(val)
        })
      }
    }
  }
</script>


<style>
.main {
  height: 100%;
  width: 100%;
  font: 14px Arial, "PingFang SC", "Hiragino Sans GB", STHeiti,
    "Microsoft YaHei", "WenQuanYi Micro Hei", sans-serif;
  color: #495060;
}

.content {
  margin: 0 20px;
}

.content .steps {
  margin-top: 50px;
}

.order-number .number-text {
  font-weight: bold;
}

.order-number .text {
  font-size: 14px;
  color: #8a8a8a;
}

.order-state .state-title {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-left span {
  font-weight: bold;
}

.title-left .text {
  color: red;
}

.state-title .title-mid {
  display: flex;
  align-items: center;
}

.title-mid img {
  width: 18px;
  height: 18px;
}

.title-mid .mid-text {
  color: red;
}

.title-mid .text {
  color: #8a8a8a;
}

.content .order-state {
  position: relative;
  margin-top: 50px;
  border-bottom: 1px solid #e9eaec;
}

.order-state .order-info {
  width: 100%;
  border-top: 1px solid #e9eaec;
  margin: 50px 0;
  display: flex;
}

.order-info img {
  width: 18px !important;
  height: 18px !important;
  margin-right: 15px;
}

.order-info .detail-title {
  height: 50px;
  line-height: 50px;
  display: flex;
  align-items: center;
}

.order-info .order-details {
  width: 50%;
  border-right: 1px solid #e9eaec;
}

.order-info .detail-cont {
  position: relative;
}

.detail-cont .detail01 {
  display: flex;
  height: 100%;
  line-height: 25px;
  margin-top: 15px;
}

.detail01 .text-width {
  width: 100%;
}

.detail02 .text-width {
  width: 100%;
}

.detail-cont .revise-addr {
  color: #4395ff;
  position: absolute;
  top: 15px;
  right: 0;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.order-info .detail-cont {
  border-top: 1px dashed #e9eaec;
  margin: 15px 20px 0 0;
}

.order-info .buyers {
  width: 50%;
  margin-left: 20px;
}

.buyers .buyers-info {
  border-top: 1px dashed #e9eaec;
  margin-top: 15px;
  position: relative;
}

.buyers .detail02 {
  display: flex;
  height: 100%;
  line-height: 25px;
  margin-top: 15px;
}

.buyers .revise-remarks {
  color: #4395ff;
  position: absolute;
  top: 15px;
  right: 0;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.order-state .item-info {
  padding-left: 70%;
  margin: 25px 0;
}

.item-info span {
  margin-bottom: 15px;
  line-height: 30px;
}

.item-info .text {
  position: absolute;
  right: 0;
}

.item-info .amount {
  font-size: 18px;
  color: red;
}

.order-log .log-title {
  height: 50px;
  width: 100%;
  line-height: 50px;
  font-weight: bold;
  border-bottom: 1px dashed #e9eaec;
}

.order-log .log-cont {
  margin-top: 15px;
  color: #4395ff;
}
</style>



