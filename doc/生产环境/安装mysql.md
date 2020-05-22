本文为大家介绍了*CentOS* 7 64位 安装 *MySQL5.7* 的详细步骤

## 1、配置YUM源

在[MySQL]官网中下载YUM源rpm安装包：http://dev.mysql.com/downloads/repo/yum/ 

\# 下载mysql源安装包

``` 
shell> wget http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
```

#安装mysql源 

```shell
shell> yum localinstall mysql57-community-release-el7-8.noarch.rpm
```

检查mysql源是否安装成功

```shell
shell> yum repolist enabled | grep "mysql.*-community.*"
```



## 2、安装MySQL

```shell
shell>  yum install mysql-community-server
```



## 3、配置默认编码为utf8 并且设置不区分大小写

修改/etc/my.cnf配置文件，在[mysqld]下添加编码配置，如下所示：

```mysql
[mysqld]

character_set_server=utf8

init_connect='SET NAMES utf8'

lower_case_table_names=1
```



## 4、启动MySQL服务

```shell
shell>  systemctl start mysqld
```



## 5、开机启动

```shell
shell>  systemctl enable mysqld 
shell>  systemctl daemon-reload
```



## 6、修改root默认密码

mysql安装完成之后，在/var/log/mysqld.log文件中给root生成了一个默认密码。通过下面的方式找到root默认密码，然后登录mysql进行修改：

```shell
shell> grep 'temporary password' /var/log/mysqld.log
```

查看到密码后用root登录修改密码

```shell
shell> mysql -uroot -p
```

```mysql
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'MyNewPass4!'; 
```

或者

```mysql
mysql> set password for 'root'@'localhost'=password('MyNewPass4!'); 
```

**注意**：mysql5.7默认安装了密码安全检查插件（validate_password），默认密码检查策略要求密码必须包含：大小写字母、数字和特殊符号，并且长度不能少于8位。否则会提示ERROR 1819 (HY000): Your password does not satisfy the current policy requirements错误



## 7、创建数据库并添加远程登录用户

默认只允许root帐户在本地登录，如果要在其它机器上连接mysql，必须修改root允许远程连接，或者添加一个允许远程连接的帐户，为了安全起见，我添加一个新的帐户：

```mysql
mysql>  create database yamidb CHARACTER SET utf8 COLLATE utf8_general_ci;
mysql>  GRANT ALL PRIVILEGES ON yamidb.* TO 'yami'@'%' IDENTIFIED BY 'Yami@2019';
```

