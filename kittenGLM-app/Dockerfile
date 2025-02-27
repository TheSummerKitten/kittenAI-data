# 基础镜像 jdk11
FROM openjdk:11-jdk-slim
# 作者
MAINTAINER TheKitten
# 配置
ENV PARAM=""
# 设置时区
ENV TZ = Asia/Shanghai

# 添加应用
ADD target/kittenGLM-app.jar /kittenGLM-app.jar

# 启动应用
ENTRYPOINT ["sh", "-c", "java -jar $JAVA_OPTS /kittenGLM-app.jar $PARAMS"]