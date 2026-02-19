# Series Analyzer (Spring Boot + REST API)

Проєкт для керування базою серіалів та студій з REST API, веб-сторінками (Thymeleaf) і можливістю імпорту даних з JSON.

## Технології
- Java 21
- Spring Boot 3.2.5 (Web, Validation, Data JPA)
- PostgreSQL
- Liquibase (міграції та сидинг)
- Thymeleaf (простий веб-інтерфейс)
- JUnit 5 + Spring Boot Test (та інтеграційні тести)

---

## 1) Старт проєкту

### Передумови
- Встановлена Java 21
- Запущений PostgreSQL (наприклад, через OpenServer / локально)
- Створена БД `series_db` (або інша — див. `.env`)

### Конфігурація через `.env`
У корені проєкту є `.env` (підтягується автоматично з `spring.config.import=optional:file:.env`).

Приклад ключових змінних (див. `.env`):
```env
APP_MODE=web
APP_PORT=9090
APP_DEBUG=true

DB_HOST=localhost
DB_PORT=5433
DB_NAME=series_db
DB_USER=postgres
DB_PASSWORD=

DB_SEED=seed
```

> **APP_PORT** встановлює порт для Spring Boot (через `server.port`).  
> **DB_SEED** керує сидингом Liquibase (див. нижче).

### Запуск (Web режим)
- В IntelliJ IDEA: запусти клас `SeriesAnalyzerApplication`
- Після старту відкрий:
  - Веб: `http://localhost:9090/`
  - Список серіалів: `http://localhost:9090/series`
  - Статистика: `http://localhost:9090/statistics`
---

## 2) База даних та Liquibase

### Міграції
Liquibase використовує master-файл:
`src/main/resources/db/changelog/db.changelog-master.yaml`

Таблиці:
- `studios`
- `series` (FK на `studios`)

### Сидинг (початкові дані)
Сидинг керується `DB_SEED`.

У `.env`:
```env
DB_SEED=seed
```

У `application.yml`:
```yaml
spring:
  liquibase:
    contexts: ${DB_SEED:false}
```

Тобто: якщо `DB_SEED=seed`, то changeset-и з контекстом `seed` будуть застосовані.

---

## 3) REST API

Base URL:
- `http://localhost:9090`

### 3.1 Studios

#### Отримати всі студії
`GET /api/v1/studios`

#### Створити студію
`POST /api/v1/studios`  
Content-Type: `application/json`

**Body:**
```json
{
  "name": "HBO",
  "country": "USA"
}
```

#### Оновити студію
`PUT /api/v1/studios/{id}`

**Body:**
```json
{
  "name": "Netflix",
  "country": "USA"
}
```

#### Видалити студію
`DELETE /api/v1/studios/{id}`

---

### 3.2 Series

#### Отримати всі серіали (пагінація/сортування)
`GET /api/v1/series?page=0&size=10&sort=rating,desc`

#### Отримати серіал по id
`GET /api/v1/series/{id}`

#### Топ серіалів
`GET /api/v1/series/top?limit=5`

#### Пошук
`GET /api/v1/series/search?query=game`

#### Створити серіал
`POST /api/v1/series`  
Content-Type: `application/json`

**Body:**
```json
{
  "title": "Wednesday",
  "genre": "Mystery",
  "seasons": 2,
  "rating": 8.1,
  "year": 2022,
  "finished": false,
  "studioId": 2
}
```

#### Оновити серіал
`PUT /api/v1/series/{id}`  
Content-Type: `application/json`

**Body:**
```json
{
  "title": "Wednesday",
  "genre": "Mystery",
  "seasons": 2,
  "rating": 8.2,
  "year": 2022,
  "finished": false,
  "studioId": 2
}
```

#### Видалити серіал
`DELETE /api/v1/series/{id}`

---

### 3.3 Пакетні операції

#### Список по фільтрах (POST _list)
`POST /api/v1/series/_list`  
Content-Type: `application/json`

**Body (приклад):**
```json
{
  "genres": ["Drama", "Mystery"],
  "yearFrom": 2015,
  "yearTo": 2024,
  "minRating": 7.5,
  "finished": false
}
```

> Поля можуть бути опційними — вказуй тільки те, що потрібно фільтрувати.

#### Генерація звіту (POST _report)
`POST /api/v1/series/_report`  
Content-Type: `application/json`

**Body (приклад):**
```json
{
  "format": "csv",
  "genres": ["Drama"],
  "minRating": 8.0
}
```

Відповідь повертає `jobId`.  
Потім можна забрати результат:

`GET /api/v1/series/_report/{jobId}`

---

### 3.4 Імпорт з JSON файлу (multipart)

`POST /api/v1/series/upload`  
Content-Type: `multipart/form-data`

**Form-data:**
- key: `file`
- type: File
- value: `series.json`

> В Postman **не виставляй вручну** `Content-Type: application/json` для multipart.  
> Postman сам поставить `multipart/form-data; boundary=...`.

У проєкті є приклад файлу для імпорту:
- `src/test/resources/import-series.json`
- або файли в папці `data/`

---

## 4) Postman

Готова колекція:
`postman/Series API.postman_collection.json`

Імпортуй її в Postman та перевір ендпоїнти.

---

## 5) Тести

Запуск тестів в IntelliJ:
- вкладка **Maven** → **Lifecycle** → `test`

Або, якщо Maven доступний у терміналі:
```bash
mvn clean test
```

Покриті граничні випадки:
- порожній файл
- некоректний JSON
- відсутні обовʼязкові атрибути
- порожній список для статистики
- невідомий атрибут статистики

---

## 6) Структура проєкту (коротко)
- `org.example.series.api` — REST API (controllers, dto, mapper, validation)
- `org.example.series.core` — доменна логіка (model, loader, export, services)
- `org.example.series.web` — веб-сторінки (Thymeleaf)
- `src/main/resources/db` — Liquibase міграції
- `src/main/resources/templates` — HTML шаблони
- `src/main/resources/static` — CSS/JS

---
