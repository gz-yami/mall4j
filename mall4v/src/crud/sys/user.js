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
    label: '用户名',
    prop: 'username',
    search: true
  }, {
    label: '邮箱',
    prop: 'email'
  }, {
    label: '手机号',
    prop: 'mobile'
  }, {
    label: '创建时间',
    prop: 'createTime'
  }, {
    label: '状态',
    prop: 'status',
    type: 'select',
    dicData: [
      {
        label: '禁用',
        value: 0
      }, {
        label: '正常',
        value: 1
      }
    ]

  }]
}
