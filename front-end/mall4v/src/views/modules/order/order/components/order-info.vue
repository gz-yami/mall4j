<template>
  <el-dialog
    v-model="visible"
    :title="!dataForm.orderNumber ? '新增' : '查看'"
    :close-on-click-modal="false"
    width="80%"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      label-width="80px"
      @keyup.enter="onSubmit()"
    >
      <div class="main">
        <div class="content">
          <div class="order-number">
            <div class="num-cont">
              <el-form-item label="订单编号:">
                <span class="text">{{ dataForm.orderNumber }}</span>
              </el-form-item>
              <el-form-item>
                <el-steps
                  :active="stepsStatus"
                  align-center
                  :process-status="dataForm.status == 6 ? 'error':'wait'"
                >
                  <el-step
                    title="提交订单"
                    :description="dataForm.orderTime"
                  />
                  <el-step
                    title="买家已付款"
                    :description="dataForm.payTime"
                  />
                  <el-step
                    v-if="dataForm.orderType !== 1"
                    title="卖家已发货"
                    :description="dataForm.dvyTime"
                  />
                  <el-step
                    v-if="dataForm.orderType !== 1"
                    title="买家已收货"
                    :description="dataForm.finallyTime"
                  />
                </el-steps>
              </el-form-item>
            </div>
          </div>
          <div class="order-state">
            <div class="state-cont">
              <div class="state-title">
                <el-form-item label="订单状态:">
                  <template #default>
                    <el-tag
                      v-if="dataForm.status === 1"
                      type="warning"
                    >
                      待付款
                    </el-tag>
                    <el-tag
                      v-if="dataForm.status === 2 && dataForm.orderType !== 1"
                      type="warning"
                    >
                      待发货
                    </el-tag>
                    <el-tag
                      v-if="dataForm.status === 3 && dataForm.orderType !== 1"
                      type="warning"
                    >
                      待收货
                    </el-tag>
                    <el-tag
                      v-if="dataForm.status === 4 && dataForm.orderType !== 1"
                      type="warning"
                    >
                      待评价
                    </el-tag>
                    <el-tag
                      v-if="dataForm.status === 5"
                      type="success"
                    >
                      成功
                    </el-tag>
                    <el-tag
                      v-if="dataForm.status === 6"
                      type="danger"
                    >
                      失败
                    </el-tag>
                  </template>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-button
                      v-if="dataForm.status === 2 && dataForm.orderType !== 1"
                      type="primary"
                      plain
                      @click="changeOrder(dataForm.orderNumber)"
                    >
                      发货
                    </el-button>
                  </el-row>
                </el-form-item>
              </div>
              <div class="order-info">
                <div class="order-details">
                  <div class="detail-title">
                    <img
                      src="~@/assets/img/car.png"
                      alt=""
                    >
                    <span class="prompt">买家付款后才可以发货</span>
                  </div>
                  <div class="detail-cont">
                    <div class="detail01">
                      <img
                        src="~@/assets/img/address.png"
                        alt=""
                      >
                      <div class="text-width">
                        <el-form-item label="收货人:">
                          <span>{{ dataForm.userAddrOrder.receiver }}</span>
                        </el-form-item>
                        <el-form-item label="手机:">
                          <span>{{ dataForm.userAddrOrder.mobile }}</span>
                        </el-form-item>
                        <el-form-item label="收货地址">
                          <span>{{ dataForm.userAddrOrder.province }}{{ dataForm.userAddrOrder.city }}{{ dataForm.userAddrOrder.area }}{{ dataForm.userAddrOrder.addr }}</span>
                        </el-form-item>
                      </div>
                    </div>
                    <div class="detail01">
                      <img
                        src="~@/assets/img/invoice.png"
                        alt=""
                      >
                      <span>不开发票</span>
                    </div>
                  </div>
                </div>
                <div class="buyers">
                  <div class="detail-title">
                    <img
                      src="~@/assets/img/buyer.png"
                      alt=""
                      style="margin-right:15px"
                    >
                    <el-form-item
                      label="买家:"
                      style="margin-top:22px"
                      label-width="44px"
                    >
                      <span>{{ dataForm.nickName }}</span>
                    </el-form-item>
                  </div>
                  <div class="buyers-info">
                    <div class="detail02">
                      <img
                        src="~@/assets/img/message.png"
                        alt=""
                      >
                      <div class="text-width">
                        <span>买家备注:</span>
                        <br>
                        <span>{{ dataForm.remarks }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="item-list">
                <el-table
                  :data="dataForm.orderItems"
                  border
                >
                  <el-table-column
                    prop=""
                    label="商品"
                  >
                    <template #default="scope">
                      <div class="prod-con">
                        <img
                          :src="resourcesUrl + scope.row.pic"
                          class="prod-img"
                        >
                        <span>{{ scope.row.prodName }}</span>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="price"
                    label="单价"
                    width="180"
                    align="center"
                  >
                    <template #default="scope">
                      <span>{{ scope.row.price }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="count"
                    label="数量"
                    width="180"
                    align="center"
                  >
                    <template #default="scope">
                      <span>{{ scope.row.prodCount }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="totalPrice"
                    label="总价"
                    width="180"
                    align="center"
                  >
                    <template #default="scope">
                      <span>{{ scope.row.productTotalAmount }}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div class="item-info">
                <el-form-item label="商品总价:">
                  <span class="text">￥{{ dataForm.total }}</span>
                </el-form-item>
                <el-form-item
                  v-if="dataForm.freightAmount"
                  label="配送费用:"
                >
                  <span class="text">￥{{ dataForm.freightAmount }}</span>
                </el-form-item>
                <el-form-item label="应付金额:">
                  <span class="text">￥{{ dataForm.actualTotal }}</span>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="order-log">
            <div class="log-title">
              <span>订单日志</span>
            </div>
            <div class="log-cont">
              <el-form-item
                v-if="dataForm.orderTime"
                label-width="10px"
              >
                <span>{{ dataForm.orderTime }} {{ dataForm.nickName }} 创建订单（成功）</span>
              </el-form-item>
              <el-form-item
                v-if="dataForm.updateTime"
                label-width="10px"
              >
                <span>{{ dataForm.updateTime }} {{ dataForm.nickName }} 订单更新（成功）</span>
              </el-form-item>
              <el-form-item
                v-if="dataForm.payTime"
                label-width="10px"
              >
                <span>{{ dataForm.payTime }} {{ dataForm.nickName }} 订单付款（成功）</span>
              </el-form-item>
              <el-form-item
                v-if="dataForm.dvyTime"
                label-width="10px"
              >
                <span>{{ dataForm.dvyTime }} {{ dataForm.nickName }} 订单发货（成功）</span>
              </el-form-item>
              <el-form-item
                v-if="dataForm.finallyTime"
                label-width="10px"
              >
                <span>{{ dataForm.finallyTime }} {{ dataForm.nickName }} 完成订单（成功）</span>
              </el-form-item>
              <el-form-item
                v-if="dataForm.cancelTime"
                label-width="10px"
              >
                <span>{{ dataForm.cancelTime }} {{ dataForm.nickName }} 取消订单（成功）</span>
              </el-form-item>
            </div>
          </div>
        </div>
      </div>
    </el-form>

    <!-- 弹窗, 新增 / 修改 -->
    <devy-add
      v-if="devyVisible"
      ref="devyAddRef"
      @refresh-data-list="getDataList"
    />
  </el-dialog>
</template>

<script setup>
import DevyAdd from './order-devy.vue'
const resourcesUrl = import.meta.env.VITE_APP_RESOURCES_URL
const dataForm = ref({
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
})

const stepsStatus = computed(() => {
  if (dataForm.value.finallyTime) {
    return 4
  }
  if (dataForm.value.dvyTime) {
    return 3
  }
  if (dataForm.value.payTime) {
    return 2
  }
  if (dataForm.value.orderTime) {
    return 1
  }
})

const visible = ref(false)
const devyVisible = ref(false)
watch(
  () => visible.value,
  () => {
    if (!visible.value) {
      devyVisible.value = false
    }
  }
)

const dataFormRef = ref(null)
const init = (orderNumber) => {
  dataForm.value.orderNumber = orderNumber || 0
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
  })
  if (dataForm.value.orderNumber) {
    // 修改
    http({
      url: http.adornUrl(`/order/order/orderInfo/${dataForm.value.orderNumber}`),
      method: 'get',
      params: http.adornParams()
    })
      .then(({ data }) => {
        dataForm.value = data
      })
  }
}
defineExpose({ init })

const getDataList = () => {
  http({
    url: http.adornUrl(`/order/order/orderInfo/${dataForm.value.orderNumber}`),
    method: 'get',
    params: http.adornParams()
  })
    .then(({ data }) => {
      dataForm.value = data
    })
}

const devyAddRef = ref(null)
/**
 * 发货
 * @param orderNumber
 */
const changeOrder = (orderNumber) => {
  devyVisible.value = true
  nextTick(() => {
    devyAddRef.value?.init(orderNumber, dataForm.value.dvyId, dataFormRef.value?.dvyFlowId)
  })
}
</script>

<style scoped lang="scss">
.main {
  height: 100%;
  width: 100%;
  font: 14px Arial, "PingFang SC", "Hiragino Sans GB", STHeiti, "Microsoft YaHei", "WenQuanYi Micro Hei", sans-serif;
  color: #495060;
}
.content {
  margin: 0 20px;
  .order-state {
    position: relative;
    margin-top: 50px;
    border-bottom: 1px solid #e9eaec;
  }
}
.order-number {
  .text {
    font-size: 14px;
    color: #8a8a8a;
  }
}
.order-state {
  .state-title {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .order-info {
    width: 100%;
    border-top: 1px solid #e9eaec;
    margin: 50px 0;
    display: flex;
  }
  .item-info {
    padding-left: 80%;
    margin: 25px 0;
  }
}
.order-info {
  img {
    width: 18px !important;
    height: 18px !important;
    margin-right: 15px;
  }
  .detail-title {
    height: 50px;
    line-height: 50px;
    display: flex;
    align-items: center;
  }
  .order-details {
    width: 50%;
    border-right: 1px solid #e9eaec;
  }
  .detail-cont {
    position: relative;
    border-top: 1px dashed #e9eaec;
    margin: 15px 20px 0 0;
  }
  .buyers {
    width: 50%;
    margin-left: 20px;
  }
}
.detail-cont {
  .detail01 {
    display: flex;
    height: 100%;
    line-height: 25px;
    margin-top: 15px;
  }
}
.detail01 {
  .text-width {
    width: 100%;
  }
}
.detail02 {
  .text-width {
    width: 100%;
  }
}
.buyers {
  .buyers-info {
    border-top: 1px dashed #e9eaec;
    margin-top: 15px;
    position: relative;
  }
  .detail02 {
    display: flex;
    height: 100%;
    line-height: 25px;
    margin-top: 15px;
  }
}
.item-info {
  span {
    margin-bottom: 15px;
    line-height: 30px;
  }
  .text {
    position: absolute;
    right: 0;
  }
}
.order-log {
  .log-title {
    height: 50px;
    width: 100%;
    line-height: 50px;
    font-weight: bold;
  }
  .log-cont {
    color: #4395ff;
  }
}
.item-list {
  .prod-con {
    display: flex;
    .prod-img {
      width: 100px;
      height: 100px;
      margin-right: 8px;
    }
  }
}
:deep(.el-steps--horizontal) {
  flex: 25% 1 1;
}
</style>
