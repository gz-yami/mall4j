<template>
  <el-dialog
    v-model="visible"
    title="选择商品"
    :modal="false"
    :close-on-click-modal="false"
  >
    <el-table
      ref="prodTableRef"
      v-loading="dataListLoading"
      :data="dataList"
      border
      style="width: 100%;"
      @selection-change="selectChangeHandle"
    >
      <el-table-column
        v-if="isSingle"
        width="50"
        header-align="center"
        align="center"
      >
        <template #default="scope">
          <div>
            <el-radio
              v-model="singleSelectProdId"
              :label="scope.row.prodId"
              @change="getSelectProdRow(scope.row)"
            >
              &nbsp;
            </el-radio>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="!isSingle"
        type="selection"
        header-align="center"
        align="center"
        width="50"
      />
      <el-table-column
        prop="prodName"
        header-align="center"
        align="center"
        label="产品名称"
      />
      <el-table-column
        align="center"
        width="140"
        label="产品图片"
      >
        <template #default="scope">
          <img
            alt=""
            :src="scope.row.pic"
            width="100"
            height="100"
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />
    <template #footer>
      <span>
        <el-button @click="visible = false">取消</el-button>
        <el-button
          type="primary"
          @click="submitProds()"
        >确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
const emit = defineEmits(['refreshSelectProds'])
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  isSingle: {
    default: false,
    type: Boolean
  }
})
const visible = ref(false)
const dataForm = reactive({
  product: ''
})
const singleSelectProdId = ref(0)
const selectProds = ref([])
const dataList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataListLoading = ref(false)
const dataListSelections = ref([])

onMounted(() => {
  getDataList()
})

/**
 * 获取数据列表
 */
const init = (selectProdParam) => {
  selectProds.value = selectProdParam
  visible.value = true
  dataListLoading.value = true
  if (selectProds.value) {
    selectProds.value?.forEach(row => {
      dataListSelections.value.push(row)
    })
  }
  getDataList()
}
defineExpose({ init })

const prodTableRef = ref(null)
const getDataList = () => {
  http({
    url: http.adornUrl('/prod/prod/page'),
    method: 'get',
    params: http.adornParams(
      Object.assign(
        {
          current: pageIndex.value,
          size: pageSize.value
        },
        {
          prodName: dataForm.prodName
        }
      )
    )
  })
    .then(({ data }) => {
      dataList.value = data.records
      totalPage.value = data.total
      dataListLoading.value = false
      if (selectProds.value) {
        nextTick(() => {
          selectProds.value?.forEach(row => {
            const index = dataList.value?.findIndex((prodItem) => prodItem.prodId === row.prodId)
            prodTableRef.value?.toggleRowSelection(dataList.value[index])
          })
        })
      }
    })
}
/**
 * 每页数
 * @param val
 */
const sizeChangeHandle = (val) => {
  pageSize.value = val
  pageIndex.value = 1
  getDataList()
}
/**
 * 当前页
 * @param val
 */
const currentChangeHandle = (val) => {
  pageIndex.value = val
  getDataList()
}
/**
 * 单选商品事件
 * @param row
 */
const getSelectProdRow = (row) => {
  dataListSelections.value = [row]
}
/**
 * 多选点击事件
 * @param selection
 */
const selectChangeHandle = (selection) => {
  dataList.value?.forEach((tableItem) => {
    const selectedProdIndex = selection.findIndex((selectedProd) => {
      if (!selectedProd) {
        return false
      }
      return selectedProd.prodId === tableItem.prodId
    })
    const dataSelectedProdIndex = dataListSelections.value?.findIndex((dataSelectedProd) => dataSelectedProd.prodId === tableItem.prodId)
    if (selectedProdIndex > -1 && dataSelectedProdIndex === -1) {
      dataListSelections.value.push(tableItem)
    } else if (selectedProdIndex === -1 && dataSelectedProdIndex > -1) {
      dataListSelections.value.splice(dataSelectedProdIndex, 1)
    }
  })
}
/**
 * 确定事件
 */
const submitProds = () => {
  if (!dataListSelections.value.length) {
    ElMessage({
      message: '请选择商品',
      type: 'error',
      duration: 1000,
      onClose: () => {}
    })
    return
  }
  const prods = []
  dataListSelections.value.forEach(item => {
    const prodIndex = prods.findIndex((prod) => prod.prodId === item.prodId)
    if (prodIndex === -1) {
      prods.push({ prodId: item.prodId, prodName: item.prodName, pic: item.pic })
    }
  })
  emit('refreshSelectProds', prods)
  dataListSelections.value = []
  visible.value = false
}

</script>
