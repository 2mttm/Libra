FROM eclipse-temurin:21
LABEL authors="2mttm"
# Копируем JAR файл из целевой директории Maven проекта в образ
ADD libra.jar /app/

# Указываем рабочую директорию
WORKDIR /app

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "libra.jar"]