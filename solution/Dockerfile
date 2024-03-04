# Используем базовый образ с JDK 17 для сборки
FROM gradle:7.4-jdk-alpine as build
WORKDIR /workspace/app

# Копируем файлы Gradle и исходный код в образ
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY src src

# Выполняем сборку проекта, пропускаем тесты
RUN gradle build -x test --no-daemon

# Используем базовый образ с JDK 17 для выполнения
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Копируем собранный jar файл из предыдущего шага
#COPY build/libs/Pulse-0.0.1-all.jar app.jar
COPY --from=build /workspace/app/build/libs/Pulse-0.0.1-all.jar app.jar

ENV SERVER_ADRESS=0.0.0.0:8080 \
    POSTGRES_JDBC_URL=jdbc:postgresql://62.109.21.83:5431/pulse \
    POSTGRES_USERNAME=K0ras1K \
    POSTGRES_PASSWORD=Shah9Sah.

EXPOSE 8080

# Запускаем приложение
CMD ["sh", "-c", "exec java -jar /app.jar"]
