# Sber Demo

## Задача
Реализовать приложение, которое выполняет CRUD операции над User (Пользователь), Payment (Платеж), Good (товар).
Имитация онлайн-магазина. Пользователь может иметь множество платежей. Множеству платежей может принадлежать множество товаров.

Стек: Java 17, Spring Boot 3.3.2 (Data JPA, Validation, Web, DevTools), Flyway (Core, PostgreSQL), 
PostgreSQL 15 (Testcontainers), MapStruct 1.5.5.Final, Lombok 1.18.36, SpringDoc OpenAPI 2.2.0, 
Testcontainers 1.19.7, AssertJ 3.26.3.

Не успел сделать интеграционные тесты для сущности Payment и User. Также добавить и настроить библиотеку Testcontainers.
Swagger-документация по ссылке: `http://localhost:8080/swagger-ui.html`

UPD: в процессе могу делать коммиты в этот репозиторий, чтобы доделать. 
### Требования
- Язык программирования: Java 11 или Java 17
- Сборка приложения: Maven или Gradle
- Документирования кода: JavaDoc
- Логирование приложения: system. out или одно из реализаций logger
- Программа должна быть написана с применением ООП, DRY, YAGNI, должна компилироваться и запускаться.
- Исходники разместить на GitVerse, должна быть видна история коммитов.
- Написаны тесты на функционал.