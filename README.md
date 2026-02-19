# 🎬 Series Analyzer

Spring Boot REST API для керування серіалами (Entity1) та студіями
(Entity2).\

# 🏗 Архітектура

Проєкт реалізований за шаровою архітектурою:

-   Controller (REST API)
-   Service (бізнес‑логіка)
-   Repository (Spring Data JPA)
-   DTO (розділення request/response моделей)
-   Liquibase (міграції бази даних)
-   Інтеграційні тести (JUnit 5)


# 🛠 Технології

-   Java 21
-   Spring Boot 3
-   PostgreSQL
-   Liquibase
-   Maven
-   JUnit 5
-   Swagger (OpenAPI)


# 🚀 Запуск проєкту

## 1️⃣ Запустити PostgreSQL (OpenServer)

Налаштування за замовчуванням:

-   Host: localhost
-   Port: 5433
-   Database: series_db
-   Username: postgres
-   Password: postgres

## 2️⃣ Запустити застосунок

В IntelliJ IDEA: - Відкрити `SeriesAnalyzerApplication` - Натиснути ▶
Run

Або через термінал:

mvn spring-boot:run

Застосунок стартує на:

http://localhost:9090


# 📌 API Endpoints

## 🏢 Studios (Entity2)

-   POST /api/v1/studios
-   GET /api/v1/studios
-   PUT /api/v1/studios/{id}
-   DELETE /api/v1/studios/{id}

## 🎬 Series (Entity1)

-   POST /api/v1/series
-   GET /api/v1/series/{id}
-   PUT /api/v1/series/{id}
-   DELETE /api/v1/series/{id}


# 📄 Пагінація та фільтрація

POST /api/v1/series/\_list

Приклад запиту:

{ "studioId": 1, "minRating": 8.0, "page": 1, "size": 20 }


# 📊 Генерація звіту

POST /api/v1/series/\_report

Повертає CSV-файл зі статистикою.


# 📥 Імпорт JSON

POST /api/v1/series/upload

Тип запиту: multipart/form-data\
Key: file


# 🧪 Інтеграційні тести

Тести знаходяться у:

src/test/java/

Покрито:

✔ CRUD операції\
✔ Пагінація\
✔ Імпорт файлу\
✔ Обробка помилок

Граничні випадки:

-   Порожній JSON файл
-   Некоректний JSON
-   Відсутній обов'язковий атрибут
-   Порожній список для статистики

Запуск тестів:

mvn test


# ⚡ Порівняння продуктивності (багатопоточність)

Експеримент з різною кількістю потоків:

  Потоки   Час (мс)   Прискорення
  -------- ---------- -------------
  1        12450      1.00x
  2        7120       1.74x
  4        4180       2.97x
  8        3890       3.20x



# 📄 Приклад statistics_by_genre.xml

```{=html}
<?xml version="1.0" encoding="UTF-8"?>
```
`<statistics>`{=html} `<genre name="Drama">`{=html}
`<count>`{=html}12`</count>`{=html}
`<averageRating>`{=html}8.13`</averageRating>`{=html}
`<minYear>`{=html}2006`</minYear>`{=html}
`<maxYear>`{=html}2023`</maxYear>`{=html} `</genre>`{=html}
`<genre name="Comedy">`{=html} `<count>`{=html}7`</count>`{=html}
`<averageRating>`{=html}7.42`</averageRating>`{=html}
`<minYear>`{=html}1998`</minYear>`{=html}
`<maxYear>`{=html}2022`</maxYear>`{=html} `</genre>`{=html}
`</statistics>`{=html}


# 📁 Структура проєкту

src ├── main │ ├── java │ └── resources └── test


# ✅ Реалізований функціонал

✔ REST API\
✔ Liquibase міграції\
✔ Імпорт JSON\
✔ Експорт CSV\
✔ Пагінація та фільтрація\
✔ Інтеграційні тести\
✔ Багатопоточна обробка\
✔ Формування XML статистики


