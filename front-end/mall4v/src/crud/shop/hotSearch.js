export const tableOption = {
  searchMenuSpan: 6,
  columnBtn: false,
  border: true,
  index: false,
  selection: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  addBtn: false,
  editBtn: false,
  delBtn: false,
  column: [
    {
      label: '热搜标题',
      prop: 'title',
      search: true
    },
    {
      label: '热搜内容',
      prop: 'content',
      search: true
    },
    {
      label: '录入时间',
      prop: 'recDate',
      sortable: true
    },
    {
      label: '顺序',
      prop: 'seq',
      sortable: true
    },
    {
      label: '启用状态',
      prop: 'status',
      type: 'select',
      slot: true,
      search: true,
      dicData: [
        {
          label: '未启用',
          value: 0
        }, {
          label: '启用',
          value: 1
        }
      ]
    }
  ]
}
