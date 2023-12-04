<template>
  <view class="container">
    <!-- 搜索框 -->
    <view class="search-bar">
      <view class="search-box">
        <input
          placeholder="输入关键字搜索"
          class="sear-input"
          confirm-type="search"
          :value="prodName"
          @confirm="toSearchProdPage"
          @input="getSearchContent"
        >
        <image
          src="@/static/images/icon/search.png"
          class="search-img"
        />
      </view>
      <text
        class="search-hint"
        @tap="goBackIndex"
      >
        取消
      </text>
    </view>

    <view class="search-display">
      <!-- 热门搜索 -->
      <view class="hot-search">
        <view class="title-text">
          热门搜索
        </view>
        <view
          v-if="hotSearchList && hotSearchList.length"
          class="hot-search-tags"
        >
          <block
            v-for="(item, index) in hotSearchList"
            :key="index"
          >
            <text
              class="tags"
              :data-name="item.content"
              @tap="onHistSearch"
            >
              {{ item.title }}
            </text>
          </block>
        </view>
        <view
          v-else
          class="search-tit-empty"
        >
          暂无数据
        </view>
      </view>

      <!-- 搜索历史 -->
      <view
        v-if="recentSearch && recentSearch.length"
        class="history-search"
      >
        <view class="title-text history-line">
          搜索历史
          <view class="clear-history">
            <image
              src="@/static/images/icon/clear-his.png"
              @tap="clearSearch"
            />
          </view>
        </view>
        <block
          v-for="(item, index) in recentSearch"
          :key="index"
        >
          <view class="his-search-tags">
            <text
              class="tags"
              :data-name="item"
              @tap="onHistSearch"
            >
              {{ item }}
            </text>
          </view>
        </block>
      </view>
    </view>
  </view>
</template>

<script setup>
const hotSearchList = ref([])
/**
 * 生命周期函数--监听页面显示
 */
onShow(() => {
  http.request({
    url: '/search/hotSearchByShopId',
    method: 'GET',
    data: {
      number: 10,
      shopId: 1,
      sort: 1
    }
  })
    .then(({ data }) => {
      hotSearchList.value = data
    })
  // 获取历史搜索
  getRecentSearch()
})

const prodName = ref('')
/**
 * 生命周期函数--监听页面隐藏
 */
onHide(() => {
  prodName.value = ''
})

const recentSearch = ref([])
/**
 * 获取历史搜索
 */
const getRecentSearch = () => {
  recentSearch.value = uni.getStorageSync('recentSearch')
}

/**
 * 搜索提交
 */
const toSearchProdPage = () => {
  if (prodName.value.trim()) {
    // 记录最近搜索
    let recentSearchStorage = uni.getStorageSync('recentSearch') || []
    recentSearchStorage = recentSearchStorage.filter(item => item !== prodName.value)
    recentSearchStorage.unshift(prodName.value)
    if (recentSearchStorage.length > 10) {
      recentSearchStorage.pop()
    }
    uni.setStorageSync('recentSearch', recentSearchStorage) // 跳转到商品列表页
    uni.navigateTo({
      url: '/pages/search-prod-show/search-prod-show?prodName=' + prodName.value
    })
  }
}

/**
 * 清空搜索历史
 */
const clearSearch = () => {
  uni.removeStorageSync('recentSearch')
  getRecentSearch()
}

/**
 * 返回首页
 */
const goBackIndex = () => {
  uni.navigateBack({
    url: '/pages/search-page/search-page'
  })
}

/**
 * 输入商品名获取数据 || 绑定输入值
 */
const getSearchContent = (e) => {
  prodName.value = e.detail.value
}

/**
 * 点击搜素历史
 */
const onHistSearch = (e) => {
  prodName.value = e.currentTarget.dataset.name
  toSearchProdPage()
}
</script>

<style scoped lang="scss">
@use './search-page.scss';
</style>
