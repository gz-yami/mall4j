<template>
  <div class="mod-log">
    <avue-crud
      ref="crudRef"
      :page="page"
      :data="dataList"
      :option="tableOption"
      @search-change="onSearch"
      @on-load="getDataList"
    />
  </div>
</template>

<script setup>
import { tableOption } from '@/crud/sys/log.js'
const dataList = ref([])
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})

onMounted(() => {
  getDataList()
})

/**
 * 获取数据列表
 */
const getDataList = (pageParam, params, done) => {
  http({
    url: http.adornUrl('/sys/log/page'),
    method: 'get',
    params: http.adornParams(
      Object.assign(
        {
          current: pageParam == null ? page.currentPage : pageParam.currentPage,
          size: pageParam == null ? page.pageSize : pageParam.pageSize
        },
        params
      )
    )
  })
    .then(({ data }) => {
      dataList.value = data.records
      page.total = data.total
      if (done) done()
    })
}
/**
 * 条件查询
 */
const onSearch = (params, done) => {
  getDataList(page, params, done)
}

</script>
