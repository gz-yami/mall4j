export const tableOption = {
  searchMenuSpan: 6,
  columnBtn: false,
  border: true,
  index: false,
  indexLabel: '序号',
  selection: true,
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
    label: '创建时间',
    prop: 'createTime'
  },
  {
    label: '姓名',
    prop: 'userName',
    search: true
  }, {
    label: '邮箱',
    prop: 'email'
  }, {
    label: '联系方式',
    prop: 'contact'
  }, {
    label: '审核',
    prop: 'status',
    search: true,
    slot: true,
    type: 'select',
    dicData: [
      {
        label: '未审核',
        value: 0
      }, {
        label: '审核通过',
        value: 1
      }
    ]
  }]
}
