# Nginx 配置

Nginx 常用于两个场景：

- 托管管理后台静态文件。
- 需要时再反向代理后端接口。

**先对齐三件事，再抄配置：**

1. `front-end/mall4v/.env.production` 里的 `VITE_APP_BASE_API` 写成什么。
2. `yami-shop-admin` 实际监听哪个端口。
3. Nginx 的 `location` 和 `proxy_pass` 必须和上面两项一致。

不要把本文两套示例拼在一起。

## 管理后台

管理后台构建：

```powershell
cd front-end\mall4v
pnpm run build
```

将构建产物部署到 Nginx 静态目录，例如：

```text
/usr/share/nginx/admin
```

## 端口对照

| 场景 | 管理端 admin | 用户端 api |
| --- | --- | --- |
| 本地 `dev`、Docker | `8085` | `8086` |
| jar 启用 `prod`（`application-prod.yml`） | `8111` | `8112` |

`proxy_pass` 必须指向 **当前进程真实端口**，不要看见文档里的 `8085` 就照抄到生产。

## 方案 A（推荐，和仓库默认一致）

当前管理后台 `front-end/mall4v/.env.production` 默认是完整地址：

```text
VITE_APP_BASE_API = 'http://127.0.0.1:8085'
```

当前 uni-app `front-end/mall4uni/.env.production` 默认是完整地址：

```text
VITE_APP_BASE_API = 'http://127.0.0.1:8086'
```

原生小程序 `front-end/mall4m` 不走 `VITE_APP_BASE_API`，接口在 `utils/config.js` 的 `domain`，默认也是 `http://127.0.0.1:8086`。

这是本地开发口径。若后端用 `prod` 启动：

- `mall4v` 改成 `http://<服务器IP>:8111`
- `mall4uni` / `mall4m` 改成 `http://<服务器IP>:8112`

两个前端不要写成同一个端口。

此时 Nginx **只托管管理后台静态文件即可**，不必配 `/api/` 或 `/apis`：

```nginx
server {
    listen 80;
    server_name admin.example.com;

    root /usr/share/nginx/admin;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8085/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

如果前端 `VITE_APP_BASE_API` 直接写完整后端地址，则不一定需要 `/api/` 代理；如果写相对路径，则需要 Nginx 配合。

## `/apis` 转发方式

旧部署文档里使用过 `/apis` 前缀来减少后台接口域名数量。当前 `front-end/mall4v/.env.production` 默认仍是完整地址：

```text
VITE_APP_BASE_API = 'http://127.0.0.1:8085'
```

如果生产环境希望改成相对路径，例如：

```text
VITE_APP_BASE_API = '/apis'
```

则需要在 Nginx 增加对应转发：

```nginx
location /apis {
    rewrite ^/apis/(.*)$ /$1 break;
    proxy_pass http://127.0.0.1:8111;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```

其中 `8111` 来自当前 `yami-shop-admin` 的 `application-prod.yml`。如果生产端口改了，Nginx 也要同步改。

## 注意事项

- 管理后台只能连管理端接口 `8085`。
- 小程序、H5、uni-app 只能连用户端接口 `8086`。
- 微信小程序正式环境通常要求 HTTPS 域名。
- 接入支付回调时，回调域名必须能被第三方支付平台访问。
