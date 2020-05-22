## Docker 获取镜像

之前提到过，[Docker Hub](https://hub.docker.com/) 上有大量的高质量的镜像可以用，这里我们就说一下怎么获取这些镜像。

从 Docker 镜像仓库获取镜像的命令是 `docker pull`。其命令格式为：
```
# docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签]
docker pull [OPTIONS] NAME[:TAG|@DIGEST]

```

具体的选项可以通过 docker pull --help 命令看到，这里我们说一下镜像名称的格式。

- Docker 镜像仓库地址：地址的格式一般是 <域名/IP>[:端口号]。默认地址是 Docker Hub。
- 仓库名：如之前所说，这里的仓库名是两段式名称，即 <用户名>/<软件名>。对于 Docker Hub，如果不给出用户名，则默认为 library，也就是官方镜像。
比如：
```
# 向docker拉取，最小化的jre 1.8的运行环境（anapsix/alpine-java 项目名称name，8_server-jre_unlimited为标签tag）
docker pull anapsix/alpine-java:8_server-jre_unlimited
```


从下载过程中可以看到我们之前提及的分层存储的概念，镜像是由多层存储所构成。下载也是一层层的去下载，并非单一文件。下载过程中给出了每一层的 ID 的前 12 位。并且下载结束后，给出该镜像完整的 sha256 的摘要，以确保下载一致性。

### 查看已下载镜像列表
`docker images`  或 `docker image ls`

```
[root@localhost ~]# docker images
REPOSITORY            TAG                      IMAGE ID            CREATED             SIZE
anapsix/alpine-java   8_server-jre_unlimited   49d744fbb526        5 months ago        126MB
```

### 删除镜像
`docker image rm IMAGE_ID|NAME [IMAGE_ID|NAME...]` 或 `docker rmi IMAGE_ID|NAME [IMAGE_ID|NAME...]`

### 清空虚悬镜像

docker在构建了一个新的镜像（名字和tag都一样的）之后，旧的那个镜像就会变成一个虚悬镜像（旧的镜像就没有名字了），此时旧的镜像就没啥用了，可以一件清空

`docker image prune`
