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
    label: '角色名称',
    prop: 'roleName',
    search: true
  }, {
    label: '备注',
    prop: 'remark'
  }, {
    label: '创建时间',
    prop: 'createTime'
  }]
}
