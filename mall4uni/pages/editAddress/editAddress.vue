<template>
<!--pages/editAddress/editAddress.wxml-->

<view class="container">
  <!--input列表 -->
  <view class="input-box">
    <view class="section">
      <text>收 货 人</text>
      <input placeholder="姓名" type="text" maxlength="15" :value="receiver" @input="onReceiverInput"></input>
    </view>
    <view class="section">
      <text>手机号码</text>
      <input placeholder="11位手机号码" type="number" maxlength="11" :value="mobile" @input="onMobileInput"></input>
    </view>
    <view class="section" @tap="translate">
      <text>所在地区</text>
      <view class="pca">{{province}} {{city}} {{area}}</view>
      <view class="animation-element-wrapper" :animation="animation" :style="'visibility:' + (show ? 'visible':'hidden')" @tap.stop="hiddenFloatView">
        <view class="animation-element" @tap.stop="nono">
          <text class="right-bt" @tap.stop="hiddenFloatView">确定</text>
          <view class="line"></view>
          <picker-view indicator-style="height: 50rpx;" :value="value" @change="bindChange" @tap.stop="nono">
            <!--省-->
            <picker-view-column>
              <view v-for="(item, index) in provArray" :key="index">
                {{item.areaName}}
              </view>
            </picker-view-column>
            <!--地级市-->
            <picker-view-column>
              <view v-for="(item, index) in cityArray" :key="index">
                {{item.areaName}}
              </view>
            </picker-view-column>
            <!--区县-->
            <picker-view-column>
              <view v-for="(item, index) in areaArray" :key="index">
                {{item.areaName}}
              </view>
            </picker-view-column>
          </picker-view>
        </view>
      </view>

      <view class="arrow">
        <image src="/static/images/icon/more.png"></image>
      </view>
    </view>
    <view class="section">
      <text>详细地址</text>
      <input placeholder="如楼号/单元/门牌号" type="text" :value="addr" @input="onAddrInput"></input>
    </view>
  </view>
  <!-- end input列表 -->
  <!-- 功能按钮 -->
  <view class="btn-box">
    <view class="keep btn" @tap="onSaveAddr">
      <text>保存收货地址</text>
    </view>

    <view class="clear btn" @tap="onDeleteAddr" v-if="addrId!=0">
      <text>删除收货地址</text>
    </view>
  </view>
  <!-- end 功能按钮 -->
</view>
</template>

<script>
// pages/editAddress/editAddress.js
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
var index = [18, 0, 0];
var t = 0;
var show = false;
var moveY = 200;

export default {
  data() {
    return {
      value: [0, 0, 0],
      provArray: [],
      cityArray: [],
      areaArray: [],
      province: "",
      city: "",
      area: "",
      provinceId: 0,
      cityId: 0,
      areaId: 0,
      receiver: "",
      mobile: "",
      addr: "",
      addrId: 0,
      animation: "",
      show: "",
      region: ""
    };
  },

  components: {},
  props: {},
  onLoad: function (options) {
    if (options.addrId) {
      uni.showLoading();
      var params = {
        url: "/p/address/addrInfo/" + options.addrId,
        method: "GET",
        data: {},
        callBack: res => {
          //console.log(res)
          this.setData({
            province: res.province,
            city: res.city,
            area: res.area,
            provinceId: res.provinceId,
            cityId: res.cityId,
            areaId: res.areaId,
            receiver: res.receiver,
            mobile: res.mobile,
            addr: res.addr,
            addrId: options.addrId
          });
          this.initCityData(res.provinceId, res.cityId, res.areaId);
          uni.hideLoading();
        }
      };
      http.request(params);
    } else {
      this.initCityData(this.provinceId, this.cityId, this.areaId);
    }
  },

  /**
  * 生命周期函数--监听页面显示
  */
  onShow: function () {},
  onReady: function () {
    this.animation = uni.createAnimation({
      transformOrigin: "50% 50%",
      duration: 0,
      timingFunction: "ease",
      delay: 0
    });
    this.animation.translateY(200 + 'vh').step();
    this.setData({
      animation: this.animation.export(),
      show: show
    });
  },
  methods: {
    initCityData: function (provinceId, cityId, areaId) {
      var ths = this;
      uni.showLoading();
      var params = {
        url: "/p/area/listByPid",
        method: "GET",
        data: {
          pid: 0
        },
        callBack: function (res) {
          //console.log(res)
          ths.setData({
            provArray: res
          });

          if (provinceId) {
            for (var index in res) {
              if (res[index].areaId == provinceId) {
                ths.setData({
                  value: [parseInt(index), ths.value[1], ths.value[2]]
                });
              }
            }
          }

          ths.getCityArray(provinceId ? provinceId : res[0].areaId, cityId, areaId);
          uni.hideLoading();
        }
      };
      http.request(params);
    },
    //滑动事件
    bindChange: function (e) {
      var ths = this;
      var val = e.detail.value; //判断滑动的是第几个column
      //若省份column做了滑动则定位到地级市和区县第一位

      if (index[0] != val[0]) {
        val[1] = 0;
        val[2] = 0; //更新数据

        ths.getCityArray(this.provArray[val[0]].areaId); //获取地级市数据
      } else {
        //若省份column未做滑动，地级市做了滑动则定位区县第一位
        if (index[1] != val[1]) {
          val[2] = 0; //更新数据

          ths.getAreaArray(this.cityArray[val[1]].areaId); //获取区县数据
        } else {}
      }

      index = val;
      this.setData({
        value: [val[0], val[1], val[2]]
      });
      ths.setData({
        province: ths.provArray[ths.value[0]].areaName,
        city: ths.cityArray[ths.value[1]].areaName,
        area: ths.areaArray[ths.value[2]].areaName,
        provinceId: ths.provArray[ths.value[0]].areaId,
        cityId: ths.cityArray[ths.value[1]].areaId,
        areaId: ths.areaArray[ths.value[2]].areaId
      });
    },
    //移动按钮点击事件
    translate: function (e) {
      if (t == 0) {
        moveY = 0;
        show = true;
        t = 1;
      } else {
        moveY = 200;
        show = false;
        t = 0;
      } // this.animation.translate(arr[0], arr[1]).step();


      this.animationEvents(this, moveY, show);
    },

    //隐藏弹窗浮层
    hiddenFloatView(e) {
      //console.log(e);
      moveY = 200;
      show = false;
      t = 0;
      this.animationEvents(this, moveY, show);
    },

    //动画事件
    animationEvents: function (that, moveY, show) {
      //console.log("moveY:" + moveY + "\nshow:" + show);
      that.animation = uni.createAnimation({
        transformOrigin: "50% 50%",
        duration: 400,
        timingFunction: "ease",
        delay: 0
      });
      that.animation.translateY(moveY + 'vh').step();
      that.setData({
        animation: that.animation.export(),
        show: show
      });
    },

    /**
     * 根据省份ID获取 城市数据
     */
    getCityArray: function (provinceId, cityId, areaId) {
      var ths = this;
      var params = {
        url: "/p/area/listByPid",
        method: "GET",
        data: {
          pid: provinceId
        },
        callBack: function (res) {
          //console.log(res)
          ths.setData({
            cityArray: res
          });

          if (cityId) {
            for (var index in res) {
              if (res[index].areaId == cityId) {
                ths.setData({
                  value: [ths.value[0], parseInt(index), ths.value[2]]
                });
              }
            }
          }

          ths.getAreaArray(cityId ? cityId : res[0].areaId, areaId);
          uni.hideLoading();
        }
      };
      http.request(params);
    },

    /**
      * 根据城市ID获取 区数据
      */
    getAreaArray: function (cityId, areaId) {
      var ths = this;
      var params = {
        url: "/p/area/listByPid",
        method: "GET",
        data: {
          pid: cityId
        },
        callBack: function (res) {
          //console.log(res)
          ths.setData({
            areaArray: res
          });

          if (areaId) {
            for (var _index in res) {
              if (res[_index].areaId == areaId) {
                ths.setData({
                  value: [ths.value[0], ths.value[1], parseInt(_index)]
                });
              }
            }

            index = ths.value;
            ths.setData({
              province: ths.province,
              city: ths.city,
              area: ths.area,
              provinceId: ths.provinceId,
              cityId: ths.cityId,
              areaId: ths.areaId
            });
          } else {
            ths.setData({
              province: ths.provArray[ths.value[0]].areaName,
              city: ths.cityArray[ths.value[1]].areaName,
              area: ths.areaArray[ths.value[2]].areaName,
              provinceId: ths.provArray[ths.value[0]].areaId,
              cityId: ths.cityArray[ths.value[1]].areaId,
              areaId: ths.areaArray[ths.value[2]].areaId
            });
          }

          uni.hideLoading();
        }
      };
      http.request(params);
    },
    bindRegionChange: function (e) {
      //console.log('picker发送选择改变，携带值为', e.detail.value)
      this.setData({
        region: e.detail.value
      });
    },

    /**
     * 保存地址
     */
    onSaveAddr: function () {
      var ths = this;
      var receiver = ths.receiver;
      var mobile = ths.mobile;
      var addr = ths.addr;

      if (!receiver.trim()) {
        this.receiver = ''
        uni.showToast({
          title: '请输入收货人姓名',
          icon: "none"
        });
        return;
      }

      if (!mobile) {
        uni.showToast({
          title: '请输入手机号码',
          icon: "none"
        });
        return;
      }

      if (mobile.length != 11) {
        uni.showToast({
          title: '请输入正确的手机号码',
          icon: "none"
        });
        return;
      }

      if (!addr.trim()) {
        this.addr = ''
        uni.showToast({
          title: '请输入详细地址',
          icon: "none"
        });
        return;
      }

      uni.showLoading();
      var url = "/p/address/addAddr";
      var method = "POST";

      if (ths.addrId != 0) {
        url = "/p/address/updateAddr";
        method = "PUT";
      } //添加或修改地址


      var params = {
        url: url,
        method: method,
        data: {
          receiver: ths.receiver,
          mobile: ths.mobile,
          addr: ths.addr,
          province: ths.province,
          provinceId: ths.provinceId,
          city: ths.city,
          cityId: ths.cityId,
          areaId: ths.areaId,
          area: ths.area,
          userType: 0,
          addrId: ths.addrId
        },
        callBack: function (res) {
          uni.hideLoading();
          uni.navigateBack({
            delta: 1
          });
        }
      };
      http.request(params);
    },
    onReceiverInput: function (e) {
      this.setData({
        receiver: e.detail.value
      });
    },
    onMobileInput: function (e) {
      this.setData({
        mobile: e.detail.value
      });
    },
    onAddrInput: function (e) {
      this.setData({
        addr: e.detail.value
      });
    },
    //删除配送地址
    onDeleteAddr: function (e) {
      var ths = this;
      uni.showModal({
        title: '',
        content: '确定要删除此收货地址吗？',
        confirmColor: "#eb2444",

        success(res) {
          if (res.confirm) {
            var addrId = ths.addrId;
            uni.showLoading();
            var params = {
              url: "/p/address/deleteAddr/" + addrId,
              method: "DELETE",
              data: {},
              callBack: function (res) {
                uni.hideLoading();
                uni.navigateBack({
                  delta: 1
                });
              }
            };
            http.request(params);
          } else if (res.cancel) {
            console.log('用户点击取消');
          }
        }

      });
    }
  }
};
</script>
<style>
@import "./editAddress.css";
</style>
