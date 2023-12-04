<template>
  <view class="container">
    <view class="recent-news">
      <block
        v-for="(item, index) in news"
        :key="index"
      >
        <view
          class="news-item"
          :data-id="item.id"
          @tap="toNewsDetail"
        >
          <view class="news-item-title">
            {{ item.title }}
          </view>
          <view class="news-item-date">
            {{ item.publishTime }}
          </view>
        </view>
      </block>
      <view
        v-if="!news || !news.length"
        class="empty"
      >
        暂无数据
      </view>
    </view>
  </view>
</template>

<script setup>
const news = ref([])
/**
 * 生命周期函数--监听页面显示
 */
onShow(() => {
  // 加载公告
  http.request({
    url: '/shop/notice/noticeList',
    method: 'GET'
  })
    .then(({ data }) => {
      news.value = data.records
    })
})

/**
 * 跳转公告详情页
 * @param e
 */
const toNewsDetail = (e) => {
  uni.navigateTo({
    url: '/pages/news-detail/news-detail?id=' + e.currentTarget.dataset.id
  })
}
</script>

<style scoped lang="scss">
@use './recent-news.scss';
</style>
