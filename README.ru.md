# Jcode

## Языки / Languages

- [English](README.md)
- [Русский](README.ru.md)

## О проекте

Простая платформа для выполнения кода, использующая Docker для изоляции выполнения. Поддерживаются языки Go, Java и Python. Проект построен на микросервисной архитектуре с использованием Apache Kafka для асинхронного взаимодействия между сервисами.

<p align="right">(<a href="#readme-top">назад к началу</a>)</p>

### Построено с использованием

- [![Java][Java]][Java-url]
- [![Spring][Spring]][Spring-url]
- [![Golang][Golang]][Golang-url]
- [![Apache-Kafka][Apache-Kafka]][Apache-Kafka-url]
- [![PostgreSQL][PostgreSQL]][PostgreSQL-url]
- [![Docker][Docker]][Docker-url]

<p align="right">(<a href="#readme-top">назад к началу</a>)</p>

### Дизайн сервиса

![jcode](./assets/jcode.png)

<p align="right">(<a href="#readme-top">назад к началу</a>)</p>

## Начало работы

Для текущей версии проекта вам нужно локально запустить проекты на Gradle и Go, а также PostgreSQL, Kafka и Zookeeper через docker-compose или локально. В будущем все сервисы будут доступны в Docker.

### Предварительные требования

- Docker
- Docker-compose
- Java 17
- Go 1.22
- Gradle

### Установка

1. Клонируйте репозиторий

   ```bash
   git clone https://github.com/St3plox/jcode
   cd jcode
   ```
2. Запустите сервера Kafka, Zookeeper и PostgreSQL. 
   ```bash
   docker-compose up -d
   ```

3. Запустите сервисы Java
   ```bash
    ./gradlew build
    ./gradlew :discovery-server:bootRun
    ./gradlew :auth-service:bootRun
    ./gradlew :api-gateway:bootRun
    ./gradlew :code-service:bootRun
    cd code-exec-worker
    go run ./cmd
   ```

<p align="right">(<a href="#readme-top">назад к началу</a>)</p>




<!-- ROADMAP -->
## Дорожная карта

- [X] Асинхронное выполнение
- [ ] Документация
- [ ] Аутентификация
- [ ] Тестирование
- [X] Дополнительные функционал(Задачи в стиле LeetCode)


Полный список предложенного функционала и известных проблем см. в разделе [open issues](https://github.com/St3plox/jcode/issues)

<p align="right">(<a href="#readme-top">назад к началу</a>)</p>


<!-- LICENSE -->
## Лицензия

Проект распространяется по лицензии MIT. Подробности см. в файле LICENSE.txt.

<p align="right">(<a href="#readme-top">назад к началу</a>)</p>



<!-- CONTACT -->
## Contact

Егор - st3pegor@gmail.com





<p align="right">(<a href="#readme-top">назад к началу</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[Java]: https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white
[Java-url]: https://www.oracle.com/java/

[Golang]: https://img.shields.io/badge/go-00ADD8?style=for-the-badge&logo=go&logoColor=white
[Golang-url]: https://golang.org/

[Apache-Kafka]: https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white
[Apache-Kafka-url]: https://kafka.apache.org/

[PostgreSQL]: https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/

[Spring]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/

[Docker]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
