本文为大家介绍了*CentOS* 7 64位 安装 *nginx*与跨域配置 的详细步骤

Nginx官方提供了Yum源

## 1、安装nginx

```shell
yum install -y nginx
```



## 2、启动Nginx并设置开机自动运行

```shell
systemctl start nginx.service
systemctl enable nginx.service
```



## 3、配置nginx

```
vi /etc/nginx/nginx.conf
```

使用上面的命令编辑nginx的配置文件，先把配置文件中的server注释掉，然后添加下面的语句

```nginx
#小程序接口的域名配置，小程序规定要https，填写对应域名，并把https证书上传至服务器
server {
        listen 443;
        server_name mall4j-api.gz-yami.com;
        ssl on;
        ssl_certificate     /usr/share/nginx/cert/xxxxxxxxxxxxxxxx.pem;
		ssl_certificate_key /usr/share/nginx/cert/xxxxxxxxxxxxxxxx.key;
        ssl_session_timeout 5m;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        ssl_prefer_server_ciphers on;
        location / {
            proxy_pass http://127.0.0.1:8112;
			proxy_set_header Host $host;
			proxy_set_header X-Real-IP $remote_addr;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }

#后台域名配置，后台vue页面代码上传至 /usr/share/nginx/admin
server {
    listen       80;
	server_name  mall4j-admin.gz-yami.com;
    root         /usr/share/nginx/admin;

    # Load configuration files for the default server block.
    include /etc/nginx/default.d/*.conf;

    location / {
    }
        
	# 跨域配置
	location /apis {
		rewrite  ^/apis/(.*)$ /$1 break;
		proxy_pass   http://127.0.0.1:8111;
    }
        
}
```



## 4、重启nginx，让配置生效

```shell
systemctl restart nginx.service
```
