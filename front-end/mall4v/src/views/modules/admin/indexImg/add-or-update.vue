<template>
  <div class="mod-index-img">
    <el-dialog
      v-model="visible"
      :title="!dataForm.imgId ? '新增' : '修改'"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dataFormRef"
        :model="dataForm"
        :rules="dataRule"
        label-width="100px"
      >
        <el-form-item
          label="轮播图片"
          prop="imgUrl"
        >
          <pic-upload v-model="dataForm.imgUrl" />
        </el-form-item>
        <el-form-item
          label="顺序"
          prop="seq"
          :rules="[
            { required: false, pattern: /\s\S+|S+\s|\S/, message: '请输入正确的顺序', trigger: 'blur' }
          ]"
        >
          <el-input v-model="dataForm.seq" />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="0">
              禁用
            </el-radio>
            <el-radio :label="1">
              正常
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group
            v-model="dataForm.type"
            @change="deleteRelation"
          >
            <el-radio :label="-1">
              无
            </el-radio>
            <el-radio :label="0">
              商品
            </el-radio>
          </el-radio-group>
          <div v-if="dataForm.relation!=null">
            <el-card
              :body-style="{ padding: '0px' }"
              style="height: 160px;width: 120px"
            >
              <img
                alt=""
                :src="card.pic"
                style="height:104px;width:100%"
              >
              <div class="card-prod-bottom">
                <span class="card-prod-name">{{ card.name }}</span>
                <el-button
                  type="text"
                  class="card-prod-name-button"
                  @click="deleteRelation"
                >
                  删除
                </el-button>
              </div>
            </el-card>
          </div>
          <div v-if="dataForm.relation==null">
            <el-button
              v-if=" dataForm.type == 0"
              @click="addProd"
            >
              选择商品
            </el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="onSubmit()"
          >
            确定
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 商品选择弹窗-->
    <prods-select
      v-if="prodsSelectVisible"
      ref="prodsSelectRef"
      :is-single="true"
      @refresh-select-prods="selectCouponProds"
    />
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { Debounce } from '@/utils/debounce'
const emit = defineEmits(['refreshDataList'])
const dataForm = ref({
  status: 1,
  des: '',
  imgUrl: '',
  seq: 0,
  imgId: 0,
  type: -1,
  relation: null
})
const dataRule = reactive({
  imgUrl: [
    { required: true, message: '轮播图片不能为空', trigger: 'blur' }
  ]
})
// 关联数据
const card = ref({
  id: 0,
  pic: '',
  name: '',
  realData: {
    prod: [],
    shop: [],
    activity: []
  }
})
const page = reactive({
  total: 0, // 总页数
  currentPage: 1, // 当前页数
  pageSize: 10 // 每页显示多少条
})
const prodsSelectVisible = ref(false)
const visible = ref(false)
const dataFormRef = ref(null)
/**
 * 获取分类数据
 * @param id
 */
const init = (id) => {
  visible.value = true
  dataForm.value.imgId = id || 0
  if (dataForm.value.imgId) {
    // 获取产品数据
    http({
      url: http.adornUrl(`/admin/indexImg/info/${dataForm.value.imgId}`),
      method: 'get'
    })
      .then(({ data }) => {
        dataForm.value = data
        if (data.relation) {
          card.value.pic = data.pic
          card.value.name = data.prodName
          card.value.id = data.relation
        }
      })
  } else {
    nextTick(() => {
      dataFormRef.value?.resetFields()
      dataForm.value.imgUrl = ''
    })
  }
}
defineExpose({ init })

/**
 * 表单提交
 */
const onSubmit = Debounce(() => {
  dataFormRef.value?.validate((valid) => {
    if (!valid) {
      return
    }
    const param = dataForm.value
    http({
      url: http.adornUrl('/admin/indexImg'),
      method: param.imgId ? 'put' : 'post',
      data: http.adornData(param)
    })
      .then(() => {
        ElMessage({
          message: '操作成功',
          type: 'success',
          duration: 1500,
          onClose: () => {
            visible.value = false
            emit('refreshDataList', page)
          }
        })
      })
  })
})
/**
 * 删除关联数据
 */
const deleteRelation = () => {
  dataForm.value.relation = null
}

const prodsSelectRef = ref(null)
/**
 * 打开选择商品
 */
const addProd = () => {
  prodsSelectVisible.value = true
  nextTick(() => {
    prodsSelectRef.value?.init(card.value.realData.prod)
  })
}
/**
 * 添加指定商品
 */
const selectCouponProds = (prods) => {
  card.value.realData.prods = prods
  if (prods.length) {
    const selectProd = prods[0]
    dataForm.value.relation = selectProd.prodId
    card.value.pic = selectProd.pic
    card.value.name = selectProd.prodName
    card.value.id = selectProd.prodId
  } else {
    card.value = {}
  }
}
</script>

<style lang="scss" scoped>
//card样式
.card-prod-bottom {
  position: relative;
  text-align: left;
  .card-prod-name {
    margin: auto;
    padding: 0 6px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 118px;
    display: inline-block;
  }
  .card-prod-name-button {
    position: absolute;
    top: 24px;
    right: 10px;
  }
}
</style>
