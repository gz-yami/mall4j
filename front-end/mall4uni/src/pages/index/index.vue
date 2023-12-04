<template>
  <view class="container">
    <view class="bg-sear">
      <view class="scrolltop">
        <view
          class="section"
          @tap="toSearchPage"
        >
          <image
            src="@/static/images/icon/search.png"
            class="search-img"
          />
          <text class="placeholder">
            搜索
          </text>
        </view>
      </view>
    </view>

    <view class="content">
      <!-- swiper -->
      <swiper
        :autoplay="autoplay"
        :indicator-color="indicatorColor"
        :interval="interval"
        :duration="duration"
        :indicator-active-color="indicatorActiveColor + ' '"
        :circular="true"
        class="pic-swiper"
        indicator-dots
        previous-margin="20rpx"
        next-margin="20rpx"
      >
        <block
          v-for="(item, index) in indexImgs"
          :key="index"
        >
          <swiper-item class="banner-item">
            <view class="img-box">
              <image
                :src="item.imgUrl"
                :data-prodid="item.relation"
                class="banner"
                @tap="toProdPage"
              />
            </view>
          </swiper-item>
        </block>
      </swiper>
      <!-- end swiper -->

      <view class="cat-item">
        <view
          class="item"
          data-sts="1"
          @tap="toClassifyPage"
        >
          <image src="@/static/images/icon/newProd.png" />
          <text>新品推荐</text>
        </view>
        <view
          class="item"
          data-sts="1"
          @tap="toClassifyPage"
        >
          <image src="@/static/images/icon/timePrice.png" />
          <text>限时特惠</text>
        </view>
        <view
          class="item"
          data-sts="3"
          @tap="toClassifyPage"
        >
          <image src="@/static/images/icon/neweveryday.png" />
          <text>每日疯抢</text>
        </view>
        <view
          class="item"
          @tap="toCouponCenter"
        >
          <image src="@/static/images/icon/newprods.png" />
          <text>领优惠券</text>
        </view>
      </view>

      <!-- 消息播放 -->
      <view
        v-if="news && news.length"
        class="message-play"
        @tap="onNewsPage"
      >
        <image
          src="@/static/images/icon/horn.png"
          class="hornpng"
        />
        <swiper
          :vertical="true"
          :autoplay="true"
          :circular="true"
          duration="1000"
          class="swiper-cont"
        >
          <block
            v-for="(item, index) in news"
            :key="index"
          >
            <swiper-item class="items">
              {{ item.title }}
            </swiper-item>
          </block>
        </swiper>
        <text class="arrow" />
      </view>
    </view>

    <view
      v-if="updata"
      class="updata"
    >
      <block
        v-for="(item, index) in taglist"
        :key="index"
      >
        <!-- 每日上新 -->
        <view
          v-if="item.style==='2' && item.prods && item.prods.length"
          class="up-to-date"
        >
          <view class="title">
            <text>{{ item.title }}</text>
            <view
              class="more-prod-cont"
              data-sts="0"
              :data-id="item.id"
              :data-title="item.title"
              @tap="toClassifyPage"
            >
              <text class="more">
                查看更多
              </text>
            </view>
          </view>
          <view class="item-cont">
            <block
              v-for="(prod, index2) in item.prods"
              :key="index2"
            >
              <view
                class="prod-item"
                :data-prodid="prod.prodId"
                @tap="toProdPage"
              >
                <view>
                  <view class="imagecont">
                    <img-show
                      :src="prod.pic"
                      :class-list="['prodimg']"
                    />
                  </view>
                  <view class="prod-text">
                    {{ prod.prodName }}
                  </view>
                  <view class="price">
                    <text class="symbol">
                      ￥
                    </text>
                    <text class="big-num">
                      {{ wxs.parsePrice(prod.price)[0] }}
                    </text>
                    <text class="small-num">
                      .{{ wxs.parsePrice(prod.price)[1] }}
                    </text>
                  </view>
                </view>
              </view>
            </block>
          </view>
        </view>

        <!-- 商城热卖 -->
        <view
          v-if="item.style==='1' && item.prods && item.prods.length"
          class="hot-sale"
        >
          <view class="title">
            <text>{{ item.title }}</text>
            <view
              class="more-prod-cont"
              data-sts="0"
              :data-id="item.id"
              :data-title="item.title"
              @tap="toClassifyPage"
            >
              <text class="more">
                更多
              </text>
              <text class="arrow" />
            </view>
          </view>
          <view class="hotsale-item-cont">
            <block
              v-for="(prod, index2) in item.prods"
              :key="index2"
            >
              <view
                class="prod-items"
                :data-prodid="prod.prodId"
                @tap="toProdPage"
              >
                <view class="hot-imagecont">
                  <img-show
                    :src="prod.pic"
                    :class-list="['hotsaleimg']"
                  />
                </view>
                <view class="hot-text">
                  <view class="hotprod-text">
                    {{ prod.prodName }}
                  </view>
                  <view class="prod-info">
                    {{ prod.brief }}
                  </view>
                  <view class="prod-text-info">
                    <view class="price">
                      <text class="symbol">
                        ￥
                      </text>
                      <text class="big-num">
                        {{ wxs.parsePrice(prod.price)[0] }}
                      </text>
                      <text class="small-num">
                        .{{ wxs.parsePrice(prod.price)[1] }}
                      </text>
                    </view>
                    <image
                      src="@/static/images/tabbar/basket-sel.png"
                      class="basket-img"
                    />
                  </view>
                </view>
              </view>
            </block>
          </view>
        </view>

        <!-- 更多宝贝 -->
        <view
          v-if="item.style==='0' && item.prods && item.prods.length"
          class="more-prod"
        >
          <view class="title">
            {{ item.title }}
          </view>
          <view class="prod-show">
            <block
              v-for="(prod, index2) in item.prods"
              :key="index2"
            >
              <view
                class="show-item"
                :data-prodid="prod.prodId"
                @tap="toProdPage"
              >
                <view class="more-prod-pic">
                  <img-show
                    :src="prod.pic"
                    :class-list="['more-pic']"
                  />
                </view>
                <view class="prod-text-right">
                  <view class="prod-text more">
                    {{ prod.prodName }}
                  </view>
                  <view class="prod-info">
                    {{ prod.brief }}
                  </view>
                  <view class="b-cart">
                    <view class="price">
                      <text class="symbol">
                        ￥
                      </text>
                      <text class="big-num">
                        {{ wxs.parsePrice(prod.price)[0] }}
                      </text>
                      <text class="small-num">
                        .{{ wxs.parsePrice(prod.price)[1] }}
                      </text>
                    </view>
                    <image
                      src="@/static/images/tabbar/basket-sel.png"
                      class="basket-img"
                      @tap.stop="addToCart(prod)"
                    />
                  </view>
                </view>
              </view>
            </block>
          </view>
        </view>
      </block>
    </view>
  </view>
</template>

<script setup>
const wxs = number()
const indicatorColor = ref('#d1e5fb')
const indicatorActiveColor = ref('#1b7dec')
const autoplay = ref(true)
const interval = ref(2000)
const duration = ref(1000)
const indexImgs = ref([])
const seq = ref(0)
const news = ref([])
const taglist = ref([])
const updata = ref(true)

onLoad(() => {
  getAllData()
})
onShow(() => {
  // #ifdef MP-WEIXIN
  uni.getSetting({
    success (res) {
      if (!res.authSetting['scope.userInfo']) {
        uni.navigateTo({
          url: '/pages/login/login'
        })
      }
    }
  })
  // #endif
  http.getCartCount() // 重新计算购物车总数量
})

onPullDownRefresh(() => {
  // 模拟加载
  setTimeout(() => {
    getAllData()
    uni.stopPullDownRefresh() // 停止下拉刷新
  }, 100)
})

const getAllData = () => {
  http.getCartCount()// 重新计算购物车总数量
  getIndexImgs()
  getNoticeList()
  getTag()
}

const toProdPage = (e) => {
  const prodid = e.currentTarget.dataset.prodid

  if (prodid) {
    uni.navigateTo({
      url: '/pages/prod/prod?prodid=' + prodid
    })
  }
}
/**
 * 加入购物车
 * @param item
 */
const addToCart = (item) => {
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/prod/prodInfo',
    method: 'GET',
    data: {
      prodId: item.prodId
    }
  })
    .then(({ data }) => {
      http.request({
        url: '/p/shopCart/changeItem',
        method: 'POST',
        data: {
          basketId: 0,
          count: 1,
          prodId: data.prodId,
          shopId: data.shopId,
          skuId: data.skuList[0].skuId
        }
      })
        .then(() => {
          uni.hideLoading()
          http.getCartCount() // 重新计算购物车总数量
          uni.showToast({
            title: '加入购物车成功',
            icon: 'none'
          })
        })
    })
}

const toCouponCenter = () => {
  uni.showToast({
    icon: 'none',
    title: '该功能未开源'
  })
}

/**
 * 跳转搜索页
 */
const toSearchPage = () => {
  uni.navigateTo({
    url: '/pages/search-page/search-page'
  })
}

/**
 * 跳转商品活动页面
 * @param e
 */
const toClassifyPage = (e) => {
  let url = '/pages/prod-classify/prod-classify?sts=' + e.currentTarget.dataset.sts
  const id = e.currentTarget.dataset.id
  const title = e.currentTarget.dataset.title

  if (id) {
    url += '&tagid=' + id + '&title=' + title
  }

  uni.navigateTo({
    url
  })
}
/**
 * 跳转公告列表页面
 */
const onNewsPage = () => {
  uni.navigateTo({
    url: '/pages/recent-news/recent-news'
  })
}

/**
 * 加载轮播图
 */
const getIndexImgs = () => {
  http.request({
    url: '/indexImgs',
    method: 'GET',
    data: {}
  })
    .then(({ data }) => {
      indexImgs.value = data
      seq.value = data
    })
}

const getNoticeList = () => {
  // 加载公告
  http.request({
    url: '/shop/notice/topNoticeList',
    method: 'GET',
    data: {}
  })
    .then(({ data }) => {
      news.value = data
    })
}

/**
 * 加载商品标题分组列表
 */
const getTag = () => {
  http.request({
    url: '/prod/tag/prodTagList',
    method: 'GET',
    data: {}
  })
    .then(({ data }) => {
      taglist.value = data
      for (let i = 0; i < data.length; i++) {
        updata.value = false
        updata.value = true
        getTagProd(data[i].id, i)
      }
    })
}

const getTagProd = (id, index) => {
  http.request({
    url: '/prod/prodListByTagId',
    method: 'GET',
    data: {
      tagId: id,
      size: 6
    }
  })
    .then(({ data }) => {
      updata.value = false
      updata.value = true
      const taglistParam = taglist.value
      taglistParam[index].prods = data.records
      taglist.value = taglistParam
    })
}
</script>

<style scoped lang="scss">
@use './index.scss';
</style>
