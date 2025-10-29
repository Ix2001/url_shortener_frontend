# URL Shortener Service

Сервис для сокращения URL с аутентификацией через Keycloak.

## Возможности

- Создание коротких ссылок
- Просмотр всех своих ссылок со статистикой
- Детальная статистика по каждой ссылке
- Удаление своих ссылок
- Отслеживание последнего доступа к ссылке
- Аутентификация через Keycloak JWT
- Swagger UI с авторизацией
- Миграции базы данных через Liquibase
- DTO и мапперы для чистой архитектуры
- Рандомные коды ссылок (4-8 символов)

## API Endpoints

### Swagger UI
- URL: `http://localhost:8080/swagger-ui.html`
- Авторизация: Bearer Token (JWT из Keycloak)

### API Endpoints
- `POST /api/shorten` - Создать короткую ссылку
- `GET /api/my-links` - Получить все мои ссылки со статистикой
- `GET /api/stats/{code}` - Детальная статистика ссылки (только для владельца)
- `DELETE /api/links/{code}` - Удалить ссылку (только для владельца)
- `GET /{code}` - Переход по короткой ссылке

## Настройка

### Переменные окружения
```bash
export DB_URL=jdbc:postgresql://localhost:5432/shortener
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

### База данных
Liquibase автоматически создаст и обновит схему базы данных при запуске.

## Запуск

```bash
./mvnw spring-boot:run
```

## Авторизация в Swagger

1. Получите JWT токен из Keycloak
2. В Swagger UI нажмите кнопку "Authorize"
3. Введите токен в формате: `Bearer YOUR_JWT_TOKEN`
4. Нажмите "Authorize"

## Структура базы данных

### Таблица `users`
- `id` (UUID) - ID пользователя из Keycloak
- `username` (VARCHAR) - Имя пользователя
- `email` (VARCHAR) - Email
- `given_name` (VARCHAR) - Имя
- `family_name` (VARCHAR) - Фамилия
- `middle_name` (VARCHAR) - Отчество
- `full_name` (VARCHAR) - Полное имя
- `phone_number` (VARCHAR) - Телефон
- `phone_number_verified` (BOOLEAN) - Подтвержден ли телефон
- `email_verified` (BOOLEAN) - Подтвержден ли email
- `gender` (VARCHAR) - Пол

### Таблица `short_links`
- `id` (BIGSERIAL) - ID ссылки
- `original_url` (VARCHAR) - Оригинальный URL
- `created_at` (TIMESTAMP) - Дата создания
- `click_count` (BIGINT) - Количество переходов
- `last_accessed` (TIMESTAMP) - Последний доступ к ссылке
- `user_id` (UUID) - ID пользователя (FK)

## Архитектура

### DTO (Data Transfer Objects)
- `ShortLinkDto` - основная информация о ссылке
- `ShortLinkStatsDto` - детальная статистика ссылки
- `UserAccountDto` - информация о пользователе
- `CreateShortLinkRequest` - запрос на создание ссылки
- `CreateShortLinkResponse` - ответ при создании ссылки
- `DeleteLinkResponse` - ответ при удалении ссылки

### Мапперы
- `ShortLinkMapper` - преобразование между `ShortLink` entity и DTO
- `UserAccountMapper` - преобразование между `UserAccount` entity и DTO

### Генерация кодов
- `RandomCodeGenerator` - генерация рандомных кодов для ссылок
- Коды состоят из 4-8 символов (буквы и цифры)
- Первый символ всегда буква для лучшей читаемости
- Гарантированная уникальность кодов

## Примеры ответов API

### POST /api/shorten
```json
{
  "url": "https://example.com"
}
```

**Ответ:**
```json
{
  "shortUrl": "/Kx9m",
  "originalUrl": "https://example.com"
}
```

### GET /api/my-links
**Ответ:**
```json
[
  {
    "id": 1,
    "code": "Kx9m",
    "shortUrl": "/Kx9m",
    "originalUrl": "https://example.com",
    "createdAt": "2025-10-29T18:00:00",
    "clickCount": 42,
    "lastAccessed": "2025-10-29T20:30:00",
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "username": "user123"
  }
]
```

### GET /api/stats/{code}
**Ответ:**
```json
{
  "code": "Kx9m",
  "shortUrl": "/Kx9m",
  "originalUrl": "https://example.com",
  "clickCount": 42,
  "createdAt": "2025-10-29T18:00:00",
  "lastAccessed": "2025-10-29T20:30:00",
  "ownerId": "123e4567-e89b-12d3-a456-426614174000",
  "ownerUsername": "user123"
}
```

### DELETE /api/links/{code}
**Ответ:**
```json
{
  "message": "Ссылка успешно удалена",
  "code": "Kx9m"
}
```
