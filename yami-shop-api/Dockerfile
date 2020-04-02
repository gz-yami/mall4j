FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER opgames(opgames.cn@gmail.com)

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /opt/projects/mall4j

WORKDIR /opt/projects/mall4j

EXPOSE 8086

ADD ./yami-shop-api/target/yami-shop-api-0.0.1-SNAPSHOT.jar ./

CMD java -jar -Xms1024m -Xmx1024m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dspring.profiles.active=docker yami-shop-api-0.0.1-SNAPSHOT.jar