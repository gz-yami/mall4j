export const tableOption = {
  border: true,
  index: false,
  selection: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  menuWidth: 350,
  align: 'center',
  refreshBtn: true,
  searchSize: 'mini',
  addBtn: false,
  editBtn: false,
  viewBtn: false,
  delBtn: false,
  props: {
    label: 'label',
    value: 'value'
  },
  column: [
    {
      label: '自提点名称',
      prop: 'addrName',
      search: true
    }, {
      label: '手机号',
      prop: 'mobile'
    }, {
      label: '省份',
      prop: 'province'
    }, {
      label: '城市',
      prop: 'city'
    }, {
      label: '区/县',
      prop: 'area'
    }, {
      label: '地址',
      prop: 'addr'
    }]
}
