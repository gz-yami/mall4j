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
    label: '用户昵称',
    prop: 'nickName',
    search: true
  }, {
    label: '用户头像',
    prop: 'pic',
    type: 'upload',
    imgWidth: 150,
    listType: 'picture-img'
  }, {
    label: '状态',
    prop: 'status',
    search: true,
    type: 'select',
    slot: true,
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
