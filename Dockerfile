FROM eclipse-temurin:21-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일을 컨테이너 내부로 복사
COPY build/libs/spring-lotto-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080 8080

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]


