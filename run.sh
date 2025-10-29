#!/bin/bash

# Экспорт переменных окружения для базы данных
export DB_PASSWORD=1234
export DB_URL=jdbc:postgresql://localhost:5433/url_shortener
export DB_USERNAME=postgres

# Запуск приложения через Maven
mvn spring-boot:run


