# Mall4j 开源版现有功能点地图

启动、401、端口问题不要读本文件。只在用户问到某个现有功能时打开。

只列仓库里已经有的入口。路径相对 Mall4j 仓库根目录。管理端 Controller 默认在：

```text
yami-shop-admin/src/main/java/com/yami/shop/admin/controller/
```

用户端 Controller 默认在：

```text
yami-shop-api/src/main/java/com/yami/shop/api/controller/
```

系统权限 Controller 在：

```text
yami-shop-sys/src/main/java/com/yami/shop/sys/controller/
```

后台页面在 `front-end/mall4v/src/views/modules/`，CRUD 配置在 `front-end/mall4v/src/crud/`。  
C 端：小程序 `front-end/mall4m/pages/`，uni-app `front-end/mall4uni/src/pages/`。

表结构以 `db/yami_shop.sql` 为准。

## 启动与使用

| 事项 | 位置 |
| --- | --- |
| 最小闭环 | MySQL `3306` → Redis `6379` → `yami-shop-admin` `8085` → `mall4v` `9527` |
| 初始化 SQL | `db/yami_shop.sql`，库名 `yami_shops` |
| 后台账号 | `admin` / `123456` |
| 管理端配置 | `yami-shop-admin/src/main/resources/application-dev.yml` |
| 用户端配置 | `yami-shop-api/src/main/resources/application-dev.yml` |
| mall4v 接口 | `front-end/mall4v/.env.development` → `VITE_APP_BASE_API` |
| mall4m 接口 | `front-end/mall4m/utils/config.js` |
| mall4uni 接口 | `front-end/mall4uni/.env.development` |
| 文档 | `doc/2-环境搭建/` |

用户端本地联调时再启动 `yami-shop-api`（`8086`）以及 mall4m 或 mall4uni。

## 商品

| 能力 | 管理端 | 用户端 | 后台页 | C 端页 | 表 |
| --- | --- | --- | --- | --- | --- |
| 商品 SPU / 发布 | `ProductController` | `ProdController` | `prod/prodList`、`prod/prodInfo` | `prod/prod` | `tz_prod` |
| SKU / 价格库存 | 随商品保存 | `SkuController` | `prod/prodInfo` 内组件 | 商品详情 | `tz_sku` |
| 分类 | `CategoryController` | `CategoryController` | `prod/category` | `category`、`prod-classify` | `tz_category` |
| 规格 | `SpecController` | — | `prod/spec` | — | `tz_prod_prop`、`tz_prod_prop_value` |
| 商品分组 | `ProdTagController`、`ProdTagReferenceController` | `ProdTagController` | `prod/prodTag` | 首页/分组展示 | `tz_prod_tag`、`tz_prod_tag_reference` |
| 商品评论 | `ProdCommController` | `ProdCommController` | `prod/prodComm` | 商品详情评论 | 评论相关表 |
| 搜索 | — | `SearchController` | — | `search-page`、`search-prod-show` | 商品表 |

品牌 `BrandController`、属性 `AttributeController` 有管理端接口，后台没有独立 `views/modules` 目录。改商品发布时跟 `prod/prodInfo` 和对应 Service，不要假设有单独菜单页。

文档：`doc/6-核心业务/2-商品与SKU.md`、`doc/7-数据模型/5-商品数据模型.md`。

## 购物车

| 项 | 位置 |
| --- | --- |
| 用户端 | `ShopCartController` |
| 表 | `tz_basket`，唯一索引 `uk_user_shop_sku(sku_id, user_id, shop_id)` |
| C 端 | mall4m / mall4uni 的 `basket` |
| 文档 | `doc/6-核心业务/3-购物车.md` |

主要接口：`/p/shopCart/info`、`changeItem`、`deleteItem`、`deleteAll`、`prodCount`、`expiryProdList`、`totalPay`。`changeItem` 的 `count` 是增量。

## 确认订单与提交订单

| 项 | 位置 |
| --- | --- |
| 用户端 | `OrderController`（`confirm`、`submit`） |
| 监听 | `ConfirmOrderListener`、`SubmitOrderListener` |
| 服务 | `OrderServiceImpl`、购物项组装在 `BasketServiceImpl` |
| C 端 | `submit-order` |
| 表 | `tz_order`、`tz_order_item`、`tz_order_settlement`、`tz_user_addr_order` |
| 文档 | `doc/6-核心业务/4-确认订单.md`、`doc/6-核心业务/5-提交订单.md` |

`OrderParam`：`basketIds` 购物车结算，`orderItem` 立即购买，`addrId` 必填。`couponIds` 为开源版保留参数，不要按已接通优惠券来实现。

库存扣减：`SkuMapper.xml`、`ProductMapper.xml` 条件更新。

## 支付

| 项 | 位置 |
| --- | --- |
| 下单支付 | `PayController`，`POST /p/order/pay`、`/p/order/normalPay` |
| 实现 | `PayServiceImpl` |
| 回调 | `PayNoticeController`（逻辑注释中） |
| 参数 | `PayParam`：订单号集合 + 支付方式，不含前端金额 |
| C 端 | `pay-result` |
| 表 | `tz_order_settlement`（`payNo`、`pay_amount`） |
| 文档 | `doc/6-核心业务/6-支付流程.md` |

当前实现会直接 `paySuccess()` 并发布 `PaySuccessOrderEvent`。这是本地跑通，不是生产三方支付。

## 订单与发货

| 能力 | 入口 | 页面 |
| --- | --- | --- |
| 我的订单 / 详情 / 用户侧操作 | `MyOrderController` | `orderList`、`order-detail` |
| 后台订单查询 / 详情 / 发货 / 导出 | 管理端 `OrderController` | `order/order` |
| 物流查询 | 管理端/用户端 `DeliveryController` | `express-delivery` |
| 超时取消、自动确认收货 | `yami-shop-admin/.../task/OrderTask.java` | — |
| 状态枚举 | `yami-shop-bean/.../enums/OrderStatus.java` | — |

文档：`doc/6-核心业务/7-订单管理.md`、`doc/7-数据模型/6-订单数据模型.md`。

`tz_order_refund`、`OrderRefund`、`OrderRefundMapper` 存在，没有退款 Controller，也没有完整退款页面。不要按已上线退款流程改。

## 会员与地址

| 能力 | 管理端 | 用户端 | 后台页 | C 端页 | 表 |
| --- | --- | --- | --- | --- | --- |
| 会员 | `UserController` | `UserController`、`UserRegisterController` | `user/user` | `user`、`register`、登录页 | `tz_user` |
| 地址 | `UserAddrController` | `AddrController` | `user` 相关 | `delivery-address`、`editAddress` | `tz_user_addr` |
| 收藏 | — | `UserCollectionController` | — | 用户相关 | `tz_user_collection` |
| 短信 | — | `SmsController` | — | 绑定手机等 | — |
| 地区 | `AreaController` | `AreaController` | `sys/area` | 地址选择 | 地区表 |

## 店铺运营

| 能力 | 管理端 | 用户端 | 后台页 | 表 |
| --- | --- | --- | --- | --- |
| 运费模板 | `TransportController` | 确认订单时计算 | `shop/transport` | `tz_transport` |
| 自提点 | `PickAddrController` | — | `shop/pickAddr` | `tz_pick_addr` |
| 公告 | `NoticeController` | `NoticeController` | `shop/notice` | `tz_notice` |
| 热搜 | `HotSearchController` | 搜索相关 | `shop/hotSearch` | `tz_hot_search` |
| 轮播 | `IndexImgController` | `IndexImgController` | `admin/indexImg` | `tz_index_img` |
| 店铺信息 | `ShopDetailController` | — | 跟店铺配置代码 | `tz_shop_detail` |
| 文件上传 | `FileController` | — | 各表单上传 | `tz_attach_file` |
| 后台留言 | `MessageController` | — | 仅有 `crud/admin/message.js`，无独立 views 目录 | 跟 `Message` 模型 |

C 端首页 `index`，公告 `recent-news`、`news-detail`。

品牌 `BrandController`、属性 `AttributeController` 见商品节；留言接口存在但后台页不完整。改这些能力时先跟现有代码，不要假设有完整菜单页。

## 权限与系统

| 能力 | Controller | 后台页 | 表 |
| --- | --- | --- | --- |
| 管理员 | `SysUserController` | `sys/user` | `tz_sys_user`、`tz_sys_user_role` |
| 角色 | `SysRoleController` | `sys/role` | `tz_sys_role`、`tz_sys_role_menu` |
| 菜单按钮 | `SysMenuController` | `sys/menu` | `tz_sys_menu` |
| 系统日志 | `SysLogController` | `sys/log` | 日志表 |
| 系统配置 | `SysConfigController` | `sys/config` | 配置表 |
| 管理端登录 | `AdminLoginController` | 登录页 | — |

权限标识走 `@PreAuthorize("@pms.hasPermission('...')")`。登录后菜单来自 `GET /sys/menu/nav`。文档：`doc/4-技术实现/1-权限体系.md`。

关系：

```text
tz_sys_user -> tz_sys_user_role -> tz_sys_role -> tz_sys_role_menu -> tz_sys_menu
```

## 部署与排查

| 场景 | 文档 |
| --- | --- |
| 选 jar 还是 Docker | `doc/8-部署运维/1-部署路径选择.md` |
| 生产配置 | `doc/8-部署运维/3-生产配置.md`（admin / api 两份 `application-prod.yml` 都要改） |
| Nginx | `doc/8-部署运维/4-Nginx配置.md` |
| 上线自查 | `doc/8-部署运维/6-上线检查题-含答案.md` |
| 401 / 验证码 / 端口 | `doc/9-故障排查/1-常见问题.md`、`4-接口地址与401问题.md` |
| 启动失败 | `doc/9-故障排查/2-启动失败.md` |
| 登录验证码 | `doc/9-故障排查/3-登录与验证码问题.md` |

当前 `docker-compose.yml` 引用了 `db/Dockerfile`，而 `db/` 只有 SQL，不能直接无脑 `docker compose up`。
---
