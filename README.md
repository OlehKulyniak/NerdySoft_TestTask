# NerdySoft_TestTask

A Spring Boot REST API for managing a simple library (authors, books, members) and borrowing/returning books.

## Features

- CRUD operations for Authors, Books, and Members.
- Borrow/return workflow with a configurable maximum number of borrowed books.
- Validation and custom exception handling.
- DTO mapping via MapStruct.
- Unit tests for the service layer.

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Data JPA (PostgreSQL)
- Lombok
- MapStruct
- Maven
- JUnit 5, Mockito

## Prerequisites

- Java 17 or later
- Maven 3.6+
- PostgreSQL database

## Configuration

Password and connection settings are defined in `src/main/resources/application.properties`.
You can also override `maxBorrowedBooks` by creating a local `.env` file at the project root:

```properties
maxBorrowedBooks=10
spring.datasource.url=jdbc:postgresql://localhost:5432/BookStoreDB
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
```

## Build & Run

Clone the repo and use the Maven Wrapper to build and start the application:

```bash
git clone <repository-url>
cd NerdySoft_TestTask
./mvnw clean package
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.
The Swagger UI will be available at 'http://localhost:8080/swagger-ui.html'

## REST API

Controllers are located in `src/main/java/cat/project/controller`. Endpoints include:

- **AuthorController**: `/authors`
- **BookController**: `/books`
- **MemberController**: `/members`
- **MemberBookController**: borrowing/returning books

See the source code for request mappings and JSON payload examples.
