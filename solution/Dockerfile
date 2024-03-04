# Базовый образ для сборки
FROM gradle:7.4-jdk-alpine as build
WORKDIR /workspace/app

# Копирование исключительно необходимых файлов для сборки
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY src src

# Сборка приложения, исключая тесты
RUN gradle build -x test --no-daemon

# Базовый образ для выполнения
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Копирование только собранного .jar файла
COPY --from=build /workspace/app/build/libs/Pulse-0.0.1-all.jar ./app.jar

ENV SERVER_ADDRESS=0.0.0.0:8080 \
    POSTGRES_JDBC_URL=jdbc:postgresql://62.109.21.83:5431/pulse \
    POSTGRES_USERNAME=K0ras1K \
    POSTGRES_PASSWORD=Shah9Sah.

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]