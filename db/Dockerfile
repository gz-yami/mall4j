FROM mysql:5.7

MAINTAINER opgames(opgames.cn@gmail.com)

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

COPY ./db/yami_shop.sql /docker-entrypoint-initdb.d