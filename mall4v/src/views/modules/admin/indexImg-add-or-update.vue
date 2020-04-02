<template>
  <div class="mod-index-img">
    <el-dialog :title="!dataForm.imgId ? '新增' : '修改'"
               :close-on-click-modal="false"
               :visible.sync="visible">
      <el-form :model="dataForm"
               ref="dataForm"
               label-width="100px">
        <el-form-item label="轮播图片">
          <pic-upload v-model="dataForm.imgUrl"></pic-upload>
        </el-form-item>
        <el-form-item label="顺序"
                      prop="seq">
          <el-input v-model="dataForm.seq"></el-input>
        </el-form-item>
        <el-form-item label="状态"
                      prop="status">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="dataForm.type"
                          @change="deleteRelation">
            <el-radio :label="-1">无</el-radio>
            <el-radio :label="0">商品</el-radio>
            <!-- <el-radio :label="1">店铺</el-radio>
            <el-radio :label="2">活动</el-radio> -->
          </el-radio-group>
          <div v-if="dataForm.relation!=null">
            <el-card :body-style="{ padding: '0px' }"
                     style="height: 160px;width: 120px">
              <img :src="card.pic"
                   style="height:104px;width:100%">
              <div class="card-prod-bottom">
                <span class="card-prod-name">{{card.name}}</span>
                <el-button type="text"
                           class="card-prod-name-button"
                           @click="deleteRelation">删除</el-button>
              </div>
            </el-card>
          </div>
          <div v-if="dataForm.relation==null">
            <el-button @click="addProd"
                       v-if=" dataForm.type == 0"
                       size="small">选择商品</el-button>
            <!-- <el-button @click="addShop"
                       v-if=" dataForm.type == 1"
                       size="small">选择店铺</el-button>
            <el-button @click="addActivity"
                       v-if="dataForm.type == 2"
                       size="small">选择活动</el-button> -->
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     @click="dataFormSubmit()">确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 商品选择弹窗-->
    <prods-select v-if="prodsSelectVisible"
                   ref="prodsSelect"
                   :isSingle="true"
                   @refreshSelectProds="selectCouponProds"></prods-select>
  </div>
</template>

<script>
import PicUpload from '@/components/pic-upload'
import ProdsSelect from '@/components/prods-select'
export default {
  data () {
    return {
      dataForm: {
        status: 1,
        des: '',
        imgUrl: '',
        seq: 0,
        imgId: 0,
        type: -1,
        relation: null
      },
      // 关联数据
      card: {
        id: 0,
        pic: '',
        name: '',
        realData: {
          prod: [],
          shop: [],
          activity: []
        }
      },
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      },
      prodsSelectVisible: false,
      visible: false
    }
  },
  components: {
    PicUpload,
    ProdsSelect
  },
  methods: {
    // 获取分类数据
    init (id) {
      this.visible = true
      this.dataForm.imgId = id || 0
      if (this.dataForm.imgId) {
        // 获取产品数据
        this.$http({
          url: this.$http.adornUrl(`/admin/indexImg/info/${this.dataForm.imgId}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.dataForm = data
          if (data.relation) {
            this.card.pic = data.pic
            this.card.name = data.prodName
            this.card.id = data.relation
          }
        })
      } else {
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          this.dataForm.imgUrl = ''
          this.relation = null
        })
      }
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        let param = this.dataForm
        this.$http({
          url: this.$http.adornUrl(`/admin/indexImg`),
          method: param.imgId ? 'put' : 'post',
          data: this.$http.adornData(param)
        }).then(({ data }) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList', this.page)
            }
          })
        })
      })
    },
    // 删除关联数据
    deleteRelation () {
      this.dataForm.relation = null
    },
    // 打开选择商品
    addProd () {
      this.prodsSelectVisible = true
      this.$nextTick(() => {
        this.$refs.prodsSelect.init(this.card.realData.prod)
      })
    },
    // 添加指定商品
    selectCouponProds (prods) {
      this.card.realData.prods = prods
      if (prods.length) {
        let selectProd = prods[0]
        this.dataForm.relation = selectProd.prodId
        this.card.pic = selectProd.pic
        this.card.name = selectProd.prodName
        this.card.id = selectProd.prodId
      } else {
        this.card = {}
        this.relation = null
      }
    },
    addShop () {
      alert('选择店铺')
    },
    addActivity () {
      alert('选择活动')
    }
  }
}
</script>
<style lang="scss">
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
