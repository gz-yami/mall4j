> 在阅读本章节前，我们回认为您已经会安装并且使用docker，如果您不会安装使用docker的话，请阅读相关章节



**如果无法理解我们所编写的 `Dockerfile`强烈的不推荐使用docker进行生产环境部署！！！**

0. 将整个项目上传到centos中，进入到项目根目录
1. 安装 `docker` (参考《docker centos 安装》)
2. 安装`docker-compose`（参考《Docker Compose 安装与卸载》）
3. 安装`open-jdk1.8`（参考《centos jdk安装》）
4. 安装`maven`（参考《通过yum安装maven》）
5. 使用 `mvn clean package -DskipTests` 命令进行打包
6. 使用 `docker-compose up` 启动项目
7. 使用nginx将请求指向特定的端口。

