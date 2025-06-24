一个基于vue、element ui 的轻量级、前后端分离、拥有完整sku和下单流程的完全开源商城后台所属前端界面




## 前言

`mall4j商城`项目致力于为中小企业打造一个完整、易于维护的开源的电商系统，采用现阶段流行技术实现。后台管理系统包含商品管理、订单管理、运费模板、规格管理、会员管理、运营管理、内容管理、统计报表、权限管理、设置等模块。


## 授权

Mall4j官网 [https://www.mall4j.com](https://www.mall4j.com)

Mall4j 使用 AGPLv3 开源，请遵守 AGPLv3 的相关条款，或者联系作者获取商业授权([https://www.mall4j.com](https://www.mall4j.com))

## 项目链接

java后台：[https://gitee.com/gz-yami/mall4j](https://gitee.com/gz-yami/mall4j)

vue后台前端：[https://gitee.com/gz-yami/mall4v](https://gitee.com/gz-yami/mall4v)

小程序：[https://gitee.com/gz-yami/mall4m](https://gitee.com/gz-yami/mall4m)

uni-app：[https://gitee.com/gz-yami/mall4uni](https://gitee.com/gz-yami/mall4uni)




## 部署教程

### 1.安装nodejs

[NodeJS](https://nodejs.org/) 项目要求最低 18.12.0，推荐 20.9.0

如果不了解怎么安装nodejs的，可以参考 [菜鸟教程的nodejs相关](https://www.runoob.com/nodejs/nodejs-install-setup.html)

### 2.启动

- 项目要求使用 [pnpm](https://www.pnpm.cn/) 包管理工具
- 使用编辑器打开项目，在根目录执行以下命令安装依赖

```
pnpm install
```

- 运行

```
pnpm run dev
```

- 部署

```
pnpm run build
```

- 如果不想使用 pnpm，请删除 `package.json` 文件中 `preinstall` 脚本后再进行安装

```json
{
    "scripts" : {
        "preinstall": "npx only-allow pnpm"  // 使用其他包管理工具（npm、yarn、cnpm等）请删除此命令
    }
}
```

## 相关截图

![登陆](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/login.png)

![订单](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/order.png)

![商品列表](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/prodList.png)

![sku](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/sku.png)

![运费模板](https://gitee.com/gz-yami/mall4j/raw/master/screenshot/transport.png)



## 提交反馈
- Mall4j商城官网 [https://www.mall4j.com](https://www.mall4j.com)


- Mall4j商城官方技术QQ 1群：722835385（3000人群已满）
- Mall4j商城官方技术QQ 2群：729888395（2000人群已满）
- Mall4j商城官方技术QQ 3群：630293864
- 如需购买商城商业版源码，请联系商务微信

![输入图片说明](https://img.mall4j.com/contact.png-v)


## 更多信息请查看Mall4j商城官网 [https://www.mall4j.com](https://www.mall4j.com)

