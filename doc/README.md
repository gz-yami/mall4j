# 编写的原因

写在前面，很多加入我们群里的人，都会问我们源码在哪里，现在仔细回答一下

1. 我们已经声明了，这是一个前后端分离的商城，而且分离的很彻底，java的后台管理系统不是jsp，使用vue写的，所以商城的后台管理在vue这个项目 https://gitee.com/gz-yami/mall4v ,启动完java，不要访问java的端口，启动vue，访问vue的端口页面，就能看到后台管理，就能上商品了！！！

2. 和上面一样的，很多人问，前端的浏览商品的页面在哪里，小程序的代码在哪，其实就在这里 https://gitee.com/gz-yami/mall4m， 还有人用vscode，idea之类的打开小程序，说我们代码跑不起来，实际上，应该用微信开发工具去打开小程序的项目，去跑小程序的代码...我太难了 :cry:  

3. 有人会问你是不是将上面两个链接藏起来了，上面两个项目的链接都在readme里面写着，可是很多人都不读。

4. 还有人说什么不知道怎么配置微信支付，支付不能回调，登陆不了，请仔细看看开发环境的安装，看看你是不是少配置了什么配置文件

5. swagger文档怎么访问不了，其实路径是/doc.html

6. 我们开源的刚上线直接申请通过了推荐项目，第一天就有100个星星，一个月就有1k的星星，大家都不是傻的，这代码是能用的，拜托...后来没怎么维护（整个流程都是好的，整个功能都是好的，都不知道维护啥），现在又开始维护了，星星也没了（如果你悄悄拿去做外包项目，觉得这个项目对你有用，就给个星星呗）



## 先确定我们下载的项目有几个项目

- mall4j：j代表java，java项目，这里面包含了小程序/后台vue连接需要的接口。
- mall4v：v代表vue项目，是后台管理员界面使用的前端项目，因为前后端分离的
- mall4m：m代表mini，小程序项目，这里的项目是小程序的项目
- jvm：java虚拟机啦~


## 1.java开发环境安装


### 1.1开发环境

以下版本是最低要求的！！！ 提问问题前请注意开发环境！！

| 工具   | 版本 |
| ------ | ---- |
| jdk    | 1.8+ |
| mysql  | 5.7+ |
| redis  | 3.2+ |
| nodejs | 8-12 |


### 1.2 安装jdk + mysql + redis + maven

如果不了解怎么安装jdk的，可以参考 [菜鸟教程的java相关](https://www.runoob.com/java/java-environment-setup.html)

如果不了解怎么安装mysql的，可以参考  [菜鸟教程的mysql相关](https://www.runoob.com/mysql/mysql-install.html) 

如果不了解怎么安装maven的，可以参考  [菜鸟教程的maven相关]( https://www.runoob.com/maven/maven-setup.html ) 

如果对于redis的安装并不了解的，可以参考 [菜鸟教程的redis相关](https://www.runoob.com/redis/redis-install.html)

安装相对简单，网上也有很多教程，这里就不多讲述。安装完按需对redis进行配置，后启动redis服务即可。

### 2.启动

- 推荐使用idea，安装lombok插件后，使用idea导入maven项目
- 将yami_shop.sql导入到mysql中，修改`application-dev.yml`更改 datasource.url、user、password
- 将mysql配置为不区分表名大小写
- 通过修改`ma.properties` 修改微信小程序信息
- 通过修改`mp.properties` 修改微信公众号信息
- 通过修改`pay.properties` 修改微信支付信息
- 通过修改`shop.properties` 修改七牛云、阿里大于等信息
- 修改`api.properties` 修改当前接口所在域名，用于支付回调
- 启动redis，端口6379
- 通过`WebApplication`启动项目后台接口，`ApiApplication` 启动项目前端接口

## 3.vue开发环境安装

这是一套正常的vue启动流程。如果你无法理解，可能要先学习一下vue...

#### 3.1 安装nodejs + 淘宝npm镜像

如果不了解怎么安装nodejs的，可以参考   [菜鸟教程的nodejs相关](https://www.runoob.com/nodejs/nodejs-install-setup.html)



安装cnpm淘宝镜像，使用命令行输入，回车：

```bash
npm install -g cnpm --registry=https://registry.npm.taobao.org
```



#### 3.2 安装依赖启动项目

使用vs code打开vue项目，进入到该项目的根目录（根目录有`package.json` 的文件）

使用cnpm安装依赖（请勿直接使用npm安装依赖，除非你能确定你的网络能够畅快访问外网），使用命令行输入，回车：

```bash
cnpm i
```

修改`config/index.js` 里面的`target`为服务器域名端口

```json
'/proxyApi': {
    target: 'http://localhost:8087/',
    changeOrigin: true,
    pathRewrite: {
    '^/proxyApi': '/'
}
```

修改`config/index.js` 里面的`host`以便于内网联调

```json
    // Various Dev Server settings
    host: '192.168.1.120', // can be overwritten by process.env.HOST
    port: 8002, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: true,
```



运行dev环境：

```bash
npm run dev
```


## 3.小程序开发环境安装

修改`project.config.json` 里面的appid为你所申请的appid，并登陆通过`微信公众平台` 将对应的开发人员设置为该小程序开发人员的角色

修改`utils/config.js` 当中的域名端口为自己服务器的域名

使用[微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)导入已下载好的项目，进行小程序开发

## 4.文档

这代码有没有文档呀？
当然有啦，你已经下载了，在doc这个文件夹上，实在不知道，我就给链接出来咯：

### [https://gitee.com/gz-yami/mall4j/tree/master/doc](https://gitee.com/gz-yami/mall4j/tree/master/doc)
