FROM eclipse-temurin:17-jdk

WORKDIR /delivery

# 프로젝트 소스 복사
COPY . .

# Gradle 캐시 최적화를 위해 Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# 프로젝트 빌드 (bootJar 실행)
RUN ./gradlew clean bootJar

# 빌드된 JAR 파일을 app.jar로 복사
RUN cp build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]