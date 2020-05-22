安装maven的前提是安装jdk，参考《linux jdk安装》

```bash
// 使用配置工具配置第三方epel源仓库
yum-config-manager --add-repo http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo
yum-config-manager --enable epel-apache-maven
// 安装maven
yum install -y apache-maven
// 验证maven，验证是否为Oracle字样，默认有版本输出
mvn -version
//最后确认下yum源地址有没被误改
yum repolist 
```

