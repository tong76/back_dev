# 1. 빌드 단계
FROM openjdk:11 AS int-build
LABEL description="Java Application builder"

# 소스코드 
RUN git clone https://github.com/tong76/back_dev.git

# 작업 디렉터리 설정
WORKDIR back_dev

# 캐시 무효화를 위해 환경 변수를 사용하여 빌드 시점에 항상 새 값이 설정되도록 함
ARG CACHE_BUST=1

# 애플리케이션 빌드
RUN chmod 700 mvnw
RUN ./mvnw clean package

# 2. 배포 단계
FROM openjdk:11-jre-slim
LABEL description="Spring Boot Application"
EXPOSE 60433
COPY --from=int-build back_dev/target/app-in-host.jar /opt/app-in-image.jar
WORKDIR /opt
ENTRYPOINT [ "java", "-jar", "app-in-image.jar" ]