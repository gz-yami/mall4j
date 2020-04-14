var http = require('../../utils/http.js');
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    item: Object,
    type: Number,
    order: Boolean,
    canUse: Boolean,
    index: Number,
    showTimeType: Number
  },

  /**
   * 组件的初始数据
   */
  data: {
    stsType: 4

  },
  // 生命周期函数，可以为函数，或一个在methods段中定义的方法名
  attached: function() {
    //console.log(this.data.item);
  },
  /**
   * 组件的方法列表
   */
  methods: {
    receiveCoupon() {
      var couponId = this.data.item.couponId;
      http.request({
        url: "/p/myCoupon/receive",
        method: "POST",
        data: couponId,
        callBack: () => {
          var coupon = this.data.item;
          coupon.canReceive = false;
          this.setData({
            item: coupon
          })
        }
      })
    },
    checkCoupon(e) {
      // this.triggerEvent('checkCoupon', this.data.index);
      this.triggerEvent('checkCoupon', {
        couponId: e.currentTarget.dataset.couponid
      });
    },
    /**
     * 立即使用
     */
    useCoupon() {
      var url = '/pages/prod-classify/prod-classify?sts=' + this.data.stsType;
      var id = this.data.item.couponId;
      var title = "优惠券活动商品";
      if (id) {
        url += "&tagid=" + id + "&title=" + title;
      }
      wx.navigateTo({
        url: url
      })

    }
  }
})