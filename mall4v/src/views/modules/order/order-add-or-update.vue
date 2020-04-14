<template>
    <el-dialog :title="!dataForm.orderId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible" width="80%">
        <div class="main">
            <div class="content">
                <!-- 订单编号 -->
                <div class="order-number">
                    <div class="num-cont">
                        <span class="number-text">订单编号：</span>
                        <span class="text">007007007007007</span>
                        <div class="steps">
                                <el-steps :active="2" align-center>
                                    <el-step title="提交订单" description="2018/11/2 18:06:22"></el-step>
                                    <el-step title="买家已付款" description="2018/11/2 18:06:22"></el-step>
                                    <el-step title="卖家已发货" description="2018/11/2 18:06:22"></el-step>
                                    <el-step title="买家已收货" description="2018/11/2 18:06:22"></el-step>
                                </el-steps>
                        </div>
                    </div>
                </div>
                <!-- 订单编号end -->

                 <!-- 订单状态 -->
                <div class="order-state">
                    <div class="state-cont">
                        <!-- 状态标题 -->
                        <div class="state-title">
                            <div class="title-left">
                                    <span>订单状态:</span>
                            <span class="text">待支付</span>
                            </div>
                            <span class="title-mid">
                                    <img src="~@/assets/img/time.png" alt="">
                                    <span class="mid-text">倒计时2天21时21分21秒</span>
                            <span class="text">，如果客户不付款订单自动关闭</span>
                            </span>
                            <span class="title-right">
                                    <!-- <button>修改</button>
                                    <button>取消</button>
                                    <button>打印</button> -->
                                    <el-row>
                                        <el-button type="primary" plain>修改</el-button>
                                        <el-button plain>取消</el-button>
                                        <el-button type="info" plain>打印</el-button>
                                    </el-row>

                                </span>
                        </div>
                        <!-- 状态标题end -->

                        <!-- 买家信息 -->
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
                                            <span>配送方式：快递发货</span>
                                            <span class="revise-addr" onclick="window.location.href='#'"><img src="~@/assets/img/revise.png" alt="">修改地址</span>
                                            <br>
                                            <span>收货人：柠檬仔</span><br>
                                            <span>手机：13726888888</span><br>
                                            <span>收货地址：广州市大学城北创业大厦美好的家美好的家加加加加加加加家居加加加加加加</span>
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
                                    <img src="~@/assets/img/buyer.png" alt="">
                                    <span class="prompt">买家：13726888888</span>
                                </div>
                                <div class="buyers-info">
                                    <div class="detail02">
                                        <img src="~@/assets/img/message.png" alt="">
                                        <div class="text-width">
                                            <span>买家备注：</span>
                                            <span class="revise-remarks" onclick="window.location.href='#'"><img src="~@/assets/img/revise.png"  alt="">修改备注</span>
                                            <br>
                                            <span>给我加多点香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜</span><br>
                                        </div>
                                    </div>
                                    <div class="detail02">
                                        <img src="~@/assets/img/remarks.png" alt="" width="18">
                                        <div class="text-width">
                                            <span>卖家备注：</span><br>
                                            <span>给我加多点香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜香菜</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 买家信息end -->

                        <!-- 商品列表 -->
                        <div class="item-list">
                            <el-table :data="tableData" border>
                                <el-table-column prop="" label="商品">
                                      <template slot-scope="scope">
                                        <img src="~@/assets/img/remarks.png" width="100" height="100"/>
                                        <span>{{scope.row.name}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="price" label="单价" width="180" align="center">
                                </el-table-column>
                                <el-table-column prop="count" label="数量" width="180" align="center">
                                </el-table-column>
                                <el-table-column prop="totalPrice" label="总价" width="180" align="center">
                                </el-table-column>
                            </el-table>
                        </div>
                        <!-- 商品列表end -->

                         <!-- 价格详情 -->
                        <div class="item-info">
                            <span>商品总价：<span class="text">￥15.00</span></span><br>
                            <span>店铺优惠：<span class="text">-￥0.00</span></span><br>
                            <span>积分抵扣：<span class="text">-￥0.00</span></span><br>
                            <span>发票费用：<span class="text">￥0.00</span></span><br>
                            <span>配送费用：<span class="text">￥0.00</span></span><br>
                            <span>应付金额：<span class="amount text">￥15.00</span></span>
                        </div>
                         <!-- 价格详情end -->
                    </div>
                </div>
                 <!-- 订单状态end -->

                 <!-- 订单日志 -->
                <div class="order-log">
                    <div class="log-title">
                        <span>订单日志</span>
                    </div>
                    <div class="log-cont">
                        <div>2018-11-2 14:19:00 13726888888 创建订单（成功）</div>
                        <div>2018-11-2 14:19:00 13726888888 创建订单（成功）</div>
                    </div>
                </div>
                <!-- 订单日志end -->
            </div>
        </div>
    </el-dialog>
</template>

<script>
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
          orderId: 0,
          remarks: '',
          prodName: '',
          total: 0,
          actualTotal: 0,
          dvyType: '',
          orderNumber: '',
          status: 1,
          userAddrOrder: []
        },
        dataRule: {
          name: [
            {
              required: true,
              message: '菜单名称不能为空',
              trigger: 'blur'
            }
          ],
          url: [
            {
              validator: validateUrl,
              trigger: 'blur'
            }
          ]
        },
        tableData: [
          {
            name: 'iphone xs max',
            price: '8699',
            count: '2',
            totalPrice: '12000'
          }
        ]
      }
    },
    methods: {
      init (orderNumber) {
        this.dataForm.orderNumber = orderNumber || 0
        this.visible = true
      },
      // 表单提交
      dataFormSubmit () {

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



