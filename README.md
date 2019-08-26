![输入图片说明](https://gitee.com/vip_title.png "在这里输入图片标题")
![输入图片说明](https://images.gitee.com/uploads/images/2019/0711/174845_6db7724e_5094767.png "屏幕截图.png")
一个基于spring boot、spring oauth2.0、mybatis、redis的轻量级、前后端分离、防范xss攻击、拥有分布式锁，为生产环境多实例完全准备，数据库为b2b2c设计，拥有完整sku和下单流程的完全开源商城


## 前言

`Mall4j`项目致力于为中小企业打造一个完整、易于维护的开源的电商系统，采用现阶段流行技术实现。后台管理系统包含商品管理、订单管理、运费模板、规格管理、会员管理、运营管理、内容管理、统计报表、权限管理、设置等模块。


## 授权

Mall4j官网 https://www.mall4j.com

Mall4j 使用 AGPLv3 开源，请遵守 AGPLv3 的相关条款，或者联系作者获取商业授权(https://www.mall4j.com)

## 项目链接

java后台：https://gitee.com/gz-yami/mall4j

vue后台前端：https://gitee.com/gz-yami/mall4v

小程序：https://gitee.com/gz-yami/mall4m



## 演示地址

 **由于我们并不希望小程序的数据被弄混乱，我们弄了两个数据库。因此，您修改了后台的商品信息，小程序并不能看到！** 

后台：<http://mall4j-admin.gz-yami.com>  账号：admin/123456

小程序：1. 扫描二维码

![小程序](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/miniQrcode.jpg)

​		2. 搜索小程序 **亚米商城** 

## 技术选型

| 技术                   | 版本   | 说明                                    |
| ---------------------- | ------ | --------------------------------------- |
| Spring Boot            | 2.1.6  | MVC核心框架                             |
| Spring Security oauth2 | 2.1.5  | 认证和授权框架                          |
| MyBatis                | 3.5.0  | ORM框架                                 |
| MyBatisPlus            | 3.1.0  | 基于mybatis，使用lambda表达式的         |
| Swagger-UI             | 2.9.2  | 文档生产工具                            |
| Hibernator-Validator   | 6.0.17 | 验证框架                                |
| redisson               | 3.10.6 | 对redis进行封装、集成分布式锁等         |
| hikari                 | 3.2.0  | 数据库连接池                            |
| log4j2                 | 2.11.2 | 更快的log日志工具                       |
| fst                    | 2.57   | 更快的序列化和反序列化工具              |
| orika                  | 1.5.4  | 更快的bean复制工具                      |
| lombok                 | 1.18.8 | 简化对象封装工具                        |
| hutool                 | 4.5.0  | 更适合国人的java工具集                  |
| swagger-bootstrap      | 1.9.3  | 基于swagger，更便于国人使用的swagger ui |



## 部署教程



### 1.开发环境

以下版本是最低要求的！！！ 提问问题前请注意开发环境！！


| 工具  | 版本 |
| ----- | ---- |
| jdk   | 1.8+ |
| mysql | 5.7+ |
| redis | 3.2+ |

### 2.启动

- 推荐使用idea，安装lombok插件后，使用idea导入maven项目
- 将yami_shop.sql导入到mysql中，修改`application-dev.yml`更改 datasource.url、user、password
- 将mysql配置为不区分表名大小写(定时任务需要)
- 通过修改`ma.properties` 修改微信小程序信息
- 通过修改`mp.properties` 修改微信公众号信息
- 通过修改`pay.properties` 修改微信支付信息
- 通过修改`shop.properties` 修改七牛云、阿里大于等信息
- 修改`api.properties` 修改当前接口所在域名，用于支付回调
- 启动redis，端口6379
- 通过`WebApplication`启动项目后台接口，`ApiApplication` 启动项目前端接口


关于如何启动可以参考视频： https://www.bilibili.com/video/av60447070

关于授权流程可以参考视频：https://www.bilibili.com/video/av60453722


## 相关截图



### 1. 后台截图

![登陆](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/login.png)

![订单](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/order.png)

![商品列表](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/prodList.png)

![sku](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/sku.png)

![运费模板](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/transport.png)



### 2. 小程序截图

![小程序截图](https://images.gitee.com/uploads/images/2019/0706/085234_4eb7509b_5094767.jpeg "小程序截图")

## 提交反馈

- QQ群：722835385

  ![输入图片说明](https://images.gitee.com/uploads/images/2019/0723/094927_e686539d_5094767.jpeg "微信图片_20190723091006.jpg")


## 后续会加入微服务框架、分布式版本

## 你的点赞鼓励，是我们前进的动力~
## 你的点赞鼓励，是我们前进的动力~
## 你的点赞鼓励，是我们前进的动力~
