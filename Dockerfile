# 使用官方的 OpenJDK 作为基础镜像
FROM openjdk:8-jre-slim

# 设置工作目录
WORKDIR /app

# 复制 Maven 构建的 jar 包到容器中
COPY target/snowflake-app-1.0.0.jar /app/snowflake-app.jar

# 暴露应用的默认端口
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "/app/snowflake-app.jar"]
