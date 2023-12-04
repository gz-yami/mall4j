<template>
  <view class="container">
    <view class="news-detail">
      <view class="news-detail-title">
        {{ news.title }}
      </view>
      <rich-text
        class="content"
        :nodes="news.content"
      />
    </view>
  </view>
</template>

<script setup>
const news = ref({
  title: '',
  content: '',
  id: null
})
/**
 * 生命周期函数--监听页面加载
 */
onLoad((options) => {
  // 加载公告详情
  http.request({
    url: '/shop/notice/info/' + options.id,
    method: 'GET'
  })
    .then(({ data }) => {
      data.content = data.content.replace(/width=/gi, 'sss=')
      data.content = data.content.replace(/height=/gi, 'sss=')
      data.content = data.content.replace(/ \/>/gi, ' style="max-width:100% !important;display:block;" />')
      news.value = data
    })
})
</script>

<style scoped lang="scss">
@use './news-detail.scss';
</style>
