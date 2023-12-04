<template>
  <view class="Mall4j container">
    <!-- 顶部子分类tab -->
    <scroll-view
      scroll-x="true"
      class="category-tit"
      :scroll-into-view="intoView"
      :scroll-with-animation="true"
    >
      <block
        v-for="(item, index) in subCategoryList"
        :key="index"
      >
        <view
          :id="'sw' + item.categoryId"
          :class="'category-item ' + (item.categoryId==categoryId? 'on':'')"
          :data-id="item.categoryId"
          @tap="onSubCategoryTap"
        >
          {{ item.categoryName }}
        </view>
      </block>
    </scroll-view>
    <!-- 商品列表 -->
    <view class="prod-item">
      <block v-if="prodList.length">
        <block
          v-for="(prod, key) in prodList"
          :key="key"
        >
          <view
            class="prod-items"
            :data-prodid="prod.prodId"
            @tap="toProdPage"
          >
            <view class="hot-imagecont">
              <image
                :src="prod.pic"
                class="hotsaleimg"
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
              </view>
            </view>
          </view>
        </block>
      </block>
      <view
        v-else
        class="empty-wrap"
      >
        暂无商品数据~
      </view>
    </view>
  </view>
</template>

<script setup>
const wxs = number()

const parentId = ref('')
const categoryId = ref(0)
/**
 * 生命周期函数--监听页面加载
 */
onLoad((options) => {
  parentId.value = options.parentId
  categoryId.value = options.categoryId
  getSubCategory()
  getProdList()
})

const current = ref(1)
const pages = ref(0)
/**
 * 页面上拉触底事件的处理函数
 */
onReachBottom(() => {
  if (current.value < pages.value) {
    current.value = current.value + 1
    getProdList()
  }
})

const intoView = ref('')
const subCategoryList = ref([])
/**
 * 获取顶栏子分类数据
 */
const getSubCategory = () => {
  http.request({
    url: '/category/categoryInfo',
    method: 'GET',
    data: {
      parentId: parentId.value
    }
  })
    .then(({ data }) => {
      subCategoryList.value = data
      nextTick(() => {
        intoView.value = 'sw' + categoryId.value
      })
    })
}

const prodList = ref([])
const isLoaded = ref(false) // 列表是否加载完毕
/**
 * 根据分类id获取商品列表数据
 */
const getProdList = () => {
  isLoaded.value = false

  http.request({
    url: '/prod/pageProd',
    method: 'GET',
    data: {
      categoryId: categoryId.value,
      current: current.value,
      size: 10,
      sort: 0,
      isAllProdType: true
    }
  })
    .then(({ data }) => {
      isLoaded.value = true
      prodList.value = data.current == 1 ? data.records : prodList.value.concat(data.records)
      current.value = data.current
      pages.value = data.pages
    })
}

/**
 * 切换顶部分类
 */
const onSubCategoryTap = (e) => {
  categoryId.value = e.currentTarget.dataset.id
  current.value = 1
  pages.value = 0
  intoView.value = 'sw' + e.currentTarget.dataset.id
  getProdList()
}

/**
 * 跳转商品下详情
 */
const toProdPage = (e) => {
  const prodid = e.currentTarget.dataset.prodid
  if (prodid) {
    uni.navigateTo({
      url: '/pages/prod/prod?prodid=' + prodid
    })
  }
}
</script>

<style lang="scss" scoped>
@import "./sub-category.scss";
</style>
