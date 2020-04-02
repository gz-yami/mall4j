export const tableOption = {
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  addBtn: false,
  editBtn: false,
  delBtn: false,
  viewBtn: false,
  column: [
    {
      label: '收货人名称',
      prop: 'receiver'
    },
    {
      label: '省',
      prop: 'province'
    },
    {
      label: '城市',
      prop: 'city'
    },
    {
      label: '区',
      prop: 'area'
    },
    {
      label: '地址',
      prop: 'addr'
    },
    {
      label: '邮编',
      prop: 'postCode'
    },
    {
      label: '手机',
      prop: 'mobile'
    },
    {
      label: '状态',
      prop: 'status',
      search: true,
      type: 'select',
      dicData: [
        {
          label: '无效',
          value: 0
        }, {
          label: '正常',
          value: 1
        }
      ]
    },
    {
      label: '默认地址',
      prop: 'commonAddr',
      dicData: [
        {
          label: '否',
          value: 0
        }, {
          label: '是',
          value: 1
        }
      ]
    },
    {
      label: '建立时间',
      prop: 'createTime'
    },
    {
      label: '更新时间',
      prop: 'updateTime'
    }
  ]
}
