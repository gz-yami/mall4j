<template>
  <div class="mod-log">
    <avue-crud ref="crud"
               :page="page"
               :data="dataList"
               :option="tableOption"
               @search-change="searchChange"
               @on-load="getDataList">
    </avue-crud>
  </div>
</template>

<script>
import { tableOption } from '@/crud/sys/log'
export default {
  data () {
    return {
      dataList: [],
      dataListLoading: false,
      selectionDataList: [],
      tableOption: tableOption,
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      }
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList (page, params) {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/sys/log/page'),
        method: 'get',
        params: this.$http.adornParams(
          Object.assign(
            {
              current: page == null ? this.page.currentPage : page.currentPage,
              size: page == null ? this.page.pageSize : page.pageSize
            },
            params
          )
        )
      }).then(({ data }) => {
        this.dataList = data.records
        this.page.total = data.total
        this.dataListLoading = false
      })
    },
    // 条件查询
    searchChange (params) {
      this.getDataList(this.page, params)
    }
  }
}
</script>
