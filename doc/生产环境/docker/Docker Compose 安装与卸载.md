目前 Docker 官方用 GO 语言 重写 了 Docker Compose，并将其作为了 docker cli 的子命令，称为 Compose V2。

安装到全局
```shell
curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-`uname -s`-`uname -m` > docker-compose
如果上面下载很慢可以用一下命令找到适合本系统的docker-compose url
echo https://github.com/docker/compose/releases/latest/download/docker-compose-`uname -s`-`uname -m`
将输出结果在浏览器打开，这样就会自动下载命名为docker-compose再上传到服务器上

sudo mv docker-compose /usr/libexec/docker/cli-plugins
sudo chmod +x /usr/libexec/docker/cli-plugins/docker-compose
sudo chown root:root /usr/libexec/docker/cli-plugins/docker-compose
```

验证和使用
```shell
$ docker compose version

Docker Compose version v2.x.x
```
如果能正常返回，说明已经可以正常使用。只要将熟悉的 docker-compose 命令替换为 docker compose，即可使用 Docker Compose。

比如`docker-compose up -d`改为`docker compose up -d`
