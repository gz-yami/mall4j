在项目中，我们曾用jemter对 `tomcat`与`undertow`进行了同等的压力测试，后发现，`undertow`比`tomcat` 的吞吐量应该要高20%，所以，我们选择了`undertow`作为我们的Serlvet容器，`undertow` 是红帽公司开发的一款基于 NIO 的高性能 Web 嵌入式服务器，这也是SpringBoot中所推荐使用的服务容器，相比其他的服务器他更加轻量级，但在默认的情况下，SpringBoot内嵌了`tomcat`服务器，所有需要先将`tomcat`进行移除。

1.在pom.xml移除`tomcat`的依赖

```
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
     <exclusions>
         <exclusion>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-tomcat</artifactId>
         </exclusion>
     </exclusions>
</dependency>   
```

2.添加Untertow 依赖

```
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-undertow</artifactId>
 </dependency>
```

3.在application.yml中对`undertow`来进行配置。
经过我们内部的测试，拥有200个线程的api项目在 i3 8100上是比较合理的，服务器随cpu性能强弱修改线程数量

```
server:
  undertow:
    worker-threads: 200
```

undertow还有很多的配置项，大家可以根据需要来进行配置。

```
# 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个线程
# 不要设置过大，如果过大，启动项目会报错：打开文件数过多
server.undertow.io-threads=16

# 阻塞任务线程池, 当执行类似servlet请求阻塞IO操作, undertow会从这个线程池中取得线程
# 它的值设置取决于系统线程执行任务的阻塞系数，默认值是IO线程数*8
server.undertow.worker-threads=256

# 以下配置会影响buffer,这些buffer会用于服务器连接的IO操作,有点类似netty的池化内存管理
# 每块buffer的空间大小,越小的空间被利用越充分，不要设置太大，以免影响其他应用，合适即可
server.undertow.buffer-size=1024

# 每个区分配的buffer数量 , 所以pool的大小是buffer-size * buffers-per-region
server.undertow.buffers-per-region=1024

# 是否分配的直接内存(NIO直接分配的堆外内存)
server.undertow.direct-buffers=true
```

