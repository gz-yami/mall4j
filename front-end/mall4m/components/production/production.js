// components/production/production.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    item:Object,
    sts:Number,
  },


  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    toProdPage: function (e) {
      var prodid = e.currentTarget.dataset.prodid;
      wx.navigateTo({
        url: '/pages/prod/prod?prodid=' + prodid,
      })
    },
  }
})
