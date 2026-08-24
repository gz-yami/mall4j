---
name: mall4j
description: "Mall4j 开源版现有功能副驾驶：本地启动、mall4v/mall4m/mall4uni、商品/SKU、购物车、下单支付、订单发货、会员、运费、权限、部署排查。适用于下载 mall4j / yami-shop 后按现有功能使用或二次开发；不要编造开源版没有的能力。"
---

# Mall4j 开源版现有功能副驾驶

给已下载本仓库的人用：按**现有功能点**说明怎么用、改哪。细节以 `doc/` 和源码为准。路径相对仓库根目录（含 `pom.xml`、`yami-shop-admin`、`front-end`、`doc`）。这是 B2C 单商户开源版，不要套 mall4cloud / 跨境版。使用通用 `SKILL.md`（Agent Skills），Cursor、Codex、Claude Code 均可加载。

## 每轮怎么做

1. 判断场景：启动 / 排查 / 部署，或某一个现有功能点。
2. 只读下方路由里该行的 1–3 篇 `doc/`。不要整目录读 `doc/`。
3. **只有问到具体功能（商品、购物车、订单等）时**，再读 [references/feature-map.md](references/feature-map.md)。启动、401、端口问题不要读地图。
4. 先说当前实现，再给基于现有文件的改法。

## 快速路由

| 场景 | 最少阅读 |
| --- | --- |
| 第一次启动、登录后台 | `doc/2-环境搭建/1-30分钟启动路线.md`、`doc/2-环境搭建/2-环境要求.md`、`doc/2-环境搭建/3-数据库初始化.md` |
| 接口地址、401、验证码 | `doc/2-环境搭建/6-前端接口地址配置.md`、`doc/9-故障排查/1-常见问题.md` |
| 启动失败 | `doc/9-故障排查/2-启动失败.md` |
| 模块怎么分 | `doc/1-项目概览/1-项目介绍.md`、`doc/1-项目概览/3-后端模块说明.md`、`doc/1-项目概览/4-前端项目说明.md` |
| 商品 / SKU / 分类 / 规格 | `doc/6-核心业务/2-商品与SKU.md` |
| 购物车 | `doc/6-核心业务/3-购物车.md` |
| 确认 / 提交订单 | `doc/6-核心业务/4-确认订单.md`、`doc/6-核心业务/5-提交订单.md` |
| 支付 | `doc/6-核心业务/6-支付流程.md` |
| 订单 / 发货 | `doc/6-核心业务/7-订单管理.md` |
| 交易总览 | `doc/6-核心业务/1-核心交易链路.md` |
| 权限 / 菜单 | `doc/4-技术实现/1-权限体系.md` |
| 改现有后台页、菜单不显示 | `doc/5-二次开发/3-二开文件位置速查.md`、`doc/5-二次开发/9-二开常见卡点.md` |
| 部署上线 | `doc/8-部署运维/1-部署路径选择.md`、`doc/8-部署运维/3-生产配置.md` |

表名、Controller、页面对应关系：问到该功能时再打开 `feature-map.md`。分层/配置按问题补 `doc/3-技术框架/` 或 `doc/4-技术实现/` 对应篇。

## 固定口径

| 端 | 位置 | 本地 |
| --- | --- | --- |
| 管理端 | `yami-shop-admin`（`WebApplication`） | `8085` |
| 用户端 | `yami-shop-api`（`ApiApplication`） | `8086` |
| 管理后台 | `front-end/mall4v` | `9527` → `8085` |
| 小程序 | `front-end/mall4m` | `utils/config.js` → `8086` |
| uni-app | `front-end/mall4uni` | `.env.*` → `8086` |
| 库 | `db/yami_shop.sql` | `yami_shops` |

后台账号 `admin` / `123456`。第一次只跑 MySQL + Redis + admin + mall4v。mall4v 接口用 `VITE_APP_BASE_API`，资源用 `VITE_APP_RESOURCES_URL`。菜单 `url` = `front-end/mall4v/src/views/modules/<url>/index.vue`。

后台功能放 `yami-shop-admin`，浏览/购物车/下单/支付放 `yami-shop-api`。规则放 Service。改菜单时同时核 `tz_sys_menu`、`@PreAuthorize`、前端 `isAuth()`。

交易主线：`商品/SKU → 购物车 → 确认订单 → 提交订单 → 支付 → 后台发货`。金额看 `tz_order_settlement`，不接受前端传应付金额。开源支付会直接 `paySuccess()`，回调在 `PayNoticeController` 里是注释；不要把同步返回或支付结果页当成生产支付成功。

库存、订单/支付状态、`OrderTask` 自动取消/确认收货，改前先读对应 `doc/6` 并跑通链路。

## 不要编

开源版没有：多商户、SaaS、跨境、分销、营销中台、完整优惠券（`couponIds` 仅保留字段）、完整退款（有 `tz_order_refund` 无 Controller）、生产微信/支付宝。不要改成 mall4cloud 微服务。用户要这些时说明现状即可。

分不清 8085/8086 或三端、要把开源支付当三方上线、或能力不在现有功能点里：先停下来问一句，再改代码。

不重写订单状态机；不把密钥写入仓库或前端；不把 `doc/` 全文贴进回答。
---
