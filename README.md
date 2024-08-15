# <div align="center">Membership Library</div>

This application manages library members and books, allowing users to borrow and return books, as well as perform CRUD operations on members and books.

## Get Started

To run the project, follow this instruction:

1. Clone the repository:
   ```bash
   git clone https://github.com/vadymhrnk/membership_library.git
   ```

2. Download [JDK](https://www.oracle.com/java/technologies/downloads/) and [Apache Maven](https://maven.apache.org/download.cgi)

3. Build the project using:
   ```bash
   mvn clean package 
   ```
   
4. Setup `src/main/resources/application.properties` file to match you PostgreSQL database:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/${DATABASE}
   spring.datasource.username=${USERNAME}
   spring.datasource.password=${PASSWORD}
   ```

5. Use terminal to run application:
   ```bash
   mvn spring-boot:run
   ```

## Technologies Used

- ### Backend Technologies
    - **Java 17**: the primary programming language for backend development.
    - **Spring Boot**: the framework for building and deploying Java-based applications with ease.
    - **Spring Boot Starter Data JPA**: starter for using Spring Data JPA with Hibernate.
    - **Spring Boot Starter Validation**: starter for validation support.
    - **Spring Boot Starter Web**: starter for building web applications, including RESTful APIs.
    - **Hibernate Validator**: a powerful and flexible framework for data validation.
    - **Liquibase Core**: a database-independent library for tracking, managing, and applying database schema changes.
    - **MapStruct**: simplifies the implementation of bean mappings, reducing manual coding effort.
    - **PostgreSQL**: the primary relational database for persisting application data.
    - **H2 Database**: an in-memory database for testing purposes.
    - **Lombok**: a tool to reduce boilerplate code, enhancing code readability and conciseness.
    - **Spring Boot Starter Test**: starter for testing Spring Boot applications.
    - **Springdoc OpenAPI UI**: an OpenAPI for generating documentation with a UI interface.

## Application Endpoints

- **Books Controller:**
    - `GET: /books` -> Retrieve a list of all books.
    - `POST: /books` -> Add a new book to the collection.
    - `GET: /books/borrowed-books/distinct` -> Retrieve distinct borrowed books.
    - `GET: /books/borrowed-books/member` -> Retrieve borrowed books for a specific member.
    - `GET: /books/borrowed-books/summary` -> Get a summary of borrowed books.
    - `PUT: /books/{id}` -> Update book details by ID.
    - `DELETE: /books/{id}` -> Delete a book by ID.

- **Members Controller:**
    - `GET: /members` -> Retrieve a list of all members.
    - `POST: /members` -> Create a new member.
    - `PUT: /members/{id}` -> Update member details by ID.
    - `DELETE: /members/{id}` -> Delete a member by ID.
    - `POST: /members/{memberId}/borrow/{bookId}` -> Borrow a book by a member.
    - `POST: /members/{memberId}/return/{bookId}` -> Return a borrowed book by a member.
