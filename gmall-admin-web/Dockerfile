FROM openjdk:8-jre
MAINTAINER lei fengyang <leifengyang@atguigu.com>
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
["java","-Xms50m -Xmx150m","-jar","/app.jar"]