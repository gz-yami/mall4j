## 安装redis

```
#安装tcl redis需要
wget http://downloads.sourceforge.net/tcl/tcl8.6.8-src.tar.gz
tar xzvf tcl8.6.8-src.tar.gz -C /usr/local/
cd  /usr/local/tcl8.6.8/unix/
./configure  
make && make install

#安装redis
wget http://download.redis.io/releases/redis-4.0.11.tar.gz
tar xzvf redis-4.0.11.tar.gz -C /usr/local/
cd  /usr/local/redis-4.0.11/
make && make test && make install
```

## redis的生产环境启动方案

要把redis作为一个系统的daemon进程去运行的，每次系统启动，redis进程一起启动

1. wget下载redis解压出来的文件夹里面有个utils，utils目录下有个redis_init_script脚本
2. 将redis_init_script脚本拷贝到linux的/etc/init.d目录中，将redis_init_script重命名为redis_6379，6379是我们希望这个redis实例监听的端口号
3. 修改redis_6379脚本的第6行的REDISPORT，设置为相同的端口号（默认就是6379）
4. 创建两个目录：/etc/redis（存放redis的配置文件），/var/redis/6379（存放redis的持久化文件）
5. 修改redis配置文件（默认在根目录下，redis.conf），拷贝到/etc/redis目录中，修改名称为6379.conf
6. 修改redis.conf中的部分配置为生产环境

```
daemonize	yes							让redis以daemon进程运行
pidfile		/var/run/redis_6379.pid 	设置redis的pid文件位置
port		6379						设置redis的监听端口号
dir 		/var/redis/6379				设置持久化文件的存储位置
```

1. 启动redis，执行cd /etc/init.d, chmod 777 redis_6379，./redis_6379 start
2. 确认redis进程是否启动，ps -ef | grep redis
3. 让redis跟随系统启动自动启动

在redis_6379脚本中，最上面，加入两行注释

```
# chkconfig:   2345 90 10

# description:  Redis is a persistent key-value database
```

执行

```
chkconfig redis_6379 on
```

## redis cli的使用

redis-cli SHUTDOWN，连接本机的6379端口停止redis进程

redis-cli -h 127.0.0.1 -p 6379 SHUTDOWN，制定要连接的ip和端口号

redis-cli PING，ping redis的端口，看是否正常

redis-cli，进入交互式命令行
