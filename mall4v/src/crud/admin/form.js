export const tableOption = {
  border: true,
  selection: true,
  index: false,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  menuWidth: 350,
  align: 'center',
  refreshBtn: true,
  searchSize: 'mini',
  addBtn: false,
  editBtn: false,
  delBtn: false,
  viewBtn: false,
  props: {
    label: 'label',
    value: 'value'
  },
  column: [{
    label: '表单名称',
    prop: 'formName',
    search: true
  }, {
    label: '按钮文本',
    prop: 'buttonName',
    search: true
  }, {
    label: '提交次数',
    prop: 'submitNum',
    type: 'select',
    dicData: [
      {
        label: '不做限制',
        value: 0
      }, {
        label: '每个IP限填一次',
        value: 1
      }
    ]
  }, {
    label: '开启验证',
    prop: 'needValidation',
    type: 'select',
    dicData: [
      {
        label: '不需要',
        value: 0
      }, {
        label: '需要',
        value: 1
      }
    ]
  }, {
    label: '提交权限',
    prop: 'submitPerm',
    type: 'select',
    dicData: [
      {
        label: '所有人',
        value: 0
      }, {
        label: '仅会员可提交',
        value: 1
      }
    ]
  }]
}
