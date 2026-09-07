# Retailer Rewards Program

A Spring Boot REST API that calculates customer reward points from transaction data and provides monthly and total reward summaries.

## Table of Contents

- [Project Overview](#project-overview)
- [Business Requirements](#business-requirements)
- [Reward Calculation Rules](#reward-calculation-rules)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [API Documentation](#api-documentation)
- [Error Handling](#error-handling)
- [Prerequisites](#prerequisites)
- [Running Locally](#running-locally)
- [Running with Docker](#running-with-docker)
- [Testing](#testing)
- [Sample Data](#sample-data)
- [Configuration](#configuration)
- [Git Branching Strategy](#git-branching-strategy)
- [Development Phases](#development-phases)
- [Design Decisions](#design-decisions)
- [Final Verification](#final-verification)

---

## Project Overview

The Retailer Rewards Program application calculates reward points earned by customers based on transaction amounts.

The application provides a versioned REST API that returns:

- Customer information
- Monthly reward points
- Total reward points

The application uses an H2 in-memory database initialized through SQL scripts.

The project was developed incrementally using feature branches and pull requests.

---

## Business Requirements

Reward points are calculated according to these rules:

1. No points are awarded for the first $50.
2. 1 point is awarded for every dollar spent between $50 and $100.
3. 2 points are awarded for every dollar spent above $100.
4. Rewards are calculated per transaction.
5. Rewards are aggregated by month.
6. The API returns monthly and total reward points for a customer.

---

## Reward Calculation Rules

| Transaction Amount | Reward Points |
|---:|---:|
| $0 | 0 |
| $49 | 0 |
| $50 | 0 |
| $51 | 1 |
| $75 | 25 |
| $99 | 49 |
| $100 | 50 |
| $101 | 52 |
| $120 | 90 |
| $150 | 150 |

### Calculation Logic

For an amount less than or equal to $50:

```text
Reward Points = 0
```

For an amount between $50 and $100:

```text
Reward Points = Amount - 50
```

For an amount greater than $100:

```text
Reward Points = 50 + ((Amount - 100) × 2)
```

### Example

For a transaction of $120:

```text
First $50       = 0 points
Next $50        = 50 points
Remaining $20   = 40 points

Total           = 90 points
```

---

## Technology Stack

| Technology | Version / Usage |
|---|---|
| Java | 21 LTS |
| Spring Boot | 4.1.0 |
| Spring Web MVC | REST API |
| Spring Data JPA | Persistence |
| H2 Database | In-memory database |
| Lombok | Boilerplate reduction |
| Maven | Build and dependency management |
| JUnit 5 | Testing |
| Mockito | Mock-based unit testing |
| MockMvc | REST API testing |
| Docker | Containerization |
| Git | Version control |

---

## Architecture

The application follows a layered architecture:

```text
                    Client
                      |
                      | HTTP
                      v
              +------------------+
              | RewardController |
              +--------+---------+
                       |
                       v
              +----------------+
              | RewardService  |
              +--------+-------+
                       |
          +------------+-------------+
          |                          |
          v                          v
+----------------------+    +----------------------+
| RewardCalculation    |    | Spring Data JPA      |
| Service              |    | Repositories         |
+----------------------+    +----------+-----------+
                                       |
                                       v
                              +-------------------+
                              | H2 Database       |
                              +-------------------+
```

### Layer Responsibilities

**Controller**
- Handles HTTP requests and responses.
- Exposes the versioned REST API.
- Delegates business operations to the service layer.

**Service**
- Looks up customers.
- Retrieves transactions.
- Coordinates reward calculation.
- Aggregates monthly and total rewards.

**Reward Calculation Service**
- Contains the core reward-point calculation rule.
- Is isolated from HTTP and database concerns.

**Repository**
- Uses Spring Data JPA for persistence.
- Provides customer and transaction access.

**Entity**
- Represents database tables and relationships.

**DTO**
- Defines the API response and error contracts.

**Exception Handling**
- Provides application-specific exceptions and centralized REST error handling.

---

## Project Structure

```text
customer-reward-points/
│
├── .dockerignore
├── .gitignore
├── Dockerfile
├── pom.xml
├── README.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/assignment/customerrewardpoints/
│   │   │       ├── RetailerRewardsProgramApplication.java
│   │   │       ├── controller/
│   │   │       │   └── RewardController.java
│   │   │       ├── dto/
│   │   │       │   ├── ApiError.java
│   │   │       │   ├── MonthlyReward.java
│   │   │       │   └── RewardSummary.java
│   │   │       ├── entity/
│   │   │       │   ├── Customer.java
│   │   │       │   └── Transaction.java
│   │   │       ├── exception/
│   │   │       │   ├── CustomerNotFoundException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── repository/
│   │   │       │   ├── CustomerRepository.java
│   │   │       │   └── TransactionRepository.java
│   │   │       └── service/
│   │   │           ├── RewardCalculationService.java
│   │   │           └── RewardService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── schema.sql
│   │       └── data.sql
│   └── test/
│       ├── java/
│       │    └── com/assignment/customerrewardpoints/
│       │        ├── RetailerRewardsProgramApplicationTests.java
│       │        ├── RewardIntegrationTest.java
│       │        ├── controller/
│       │        │   └── RewardControllerTest.java
│       │        └── service/
│       │           ├── RewardCalculationServiceTest.java
│       │           └── RewardServiceTest.java
│       └── resources/
│           └── application-test.properties
│          
│          
│
└── target/
    └── generated by Maven; not committed
```

---

## Database Design

The application uses an H2 in-memory database.

### Customers

```text
customers
----------------
id
name
```

### Transactions

```text
transactions
-------------------------
id
customer_id
amount
transaction_date
```

### Relationship

```text
Customer
   |
   | 1
   |
   | *
   v
Transaction
```

One customer can have multiple transactions.

Each transaction belongs to exactly one customer.

### Database Initialization

Schema:

```text
src/main/resources/schema.sql
```

Seed data:

```text
src/main/resources/data.sql
```

Hibernate automatic schema generation is disabled:

```properties
spring.jpa.hibernate.ddl-auto=none
```

---

## API Documentation

### Get Customer Rewards

```http
GET /api/v1/rewards/{customerId}
```

Example:

```http
GET http://localhost:8080/api/v1/rewards/1
```

No request body is required.

### Successful Response

```json
{
  "customerId": 1,
  "customerName": "Anurag Ratna",
  "monthlyRewards": [
    {
      "month": "2026-06",
      "points": 120
    },
    {
      "month": "2026-07",
      "points": 160
    },
    {
      "month": "2026-08",
      "points": 52
    }
  ],
  "totalPoints": 332
}
```

HTTP status:

```text
200 OK
```

---

## Error Handling

The application uses a centralized `GlobalExceptionHandler`.

### Customer Not Found

```http
GET /api/v1/rewards/999
```

Response:

```text
404 Not Found
```

Example:

```json
{
  "timestamp": "2026-08-09T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 999",
  "path": "/api/v1/rewards/999"
}
```

### Negative Customer ID

```http
GET /api/v1/rewards/-1
```

Response:

```text
400 Bad Request
```

### Non-numeric Customer ID

```http
GET /api/v1/rewards/abc
```

Response:

```text
400 Bad Request
```

---

## Prerequisites

For normal local execution:

- Java 21 or later
- Maven 3.9+
- Git

Docker is optional.

No externally installed MySQL or PostgreSQL database is required.

The application uses H2 in-memory storage.

---

## Running Locally

### 1. Clone the Repository

```bash
git clone <repository-url>
cd customer-reward-points
```

Replace `<repository-url>` with the GitHub repository URL.

### 2. Verify Java

```bash
java --version
```

Java 21 is recommended.

### 3. Verify Maven

```bash
mvn --version
```

Maven 3.9+ is recommended.

### 4. Build and Verify

```bash
mvn clean verify
```

Expected:

```text
BUILD SUCCESS
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

### 6. Test the API

```bash
curl http://localhost:8080/api/v1/rewards/1
```

---

## Running with Docker

Docker is an additional deployment option. The application can also be run without Docker using Maven.

### 1. Build the Application JAR

```bash
mvn clean package
```

### 2. Build the Docker Image

```bash
docker build -t retailer-rewards-program:1.0 .
```

### 3. Run the Container

```bash
docker run --name retailer-rewards-program -p 8080:8080 retailer-rewards-program:1.0
```

### 4. Test the Containerized Application

```bash
curl http://localhost:8080/api/v1/rewards/1
```

### 5. View Logs

```bash
docker logs retailer-rewards-program
```

### 6. Stop the Container

```bash
docker stop retailer-rewards-program
```

### 7. Remove the Container

```bash
docker rm retailer-rewards-program
```

---

## Testing

The project contains multiple levels of automated testing.

### Unit Tests

**RewardCalculationServiceTest**
- Below $50
- Exactly $50
- $51
- $99
- Exactly $100
- $101
- Amount above $100
- Zero amount
- Negative amount
- Null amount

**RewardServiceTest**
- Customer lookup
- Transaction retrieval
- Monthly aggregation
- Total reward calculation
- Customer-not-found scenario

### Controller Tests

`RewardControllerTest` verifies:

- Successful API request
- HTTP status
- JSON response
- Negative customer ID
- Non-numeric customer ID

### Integration Tests

`RewardIntegrationTest` verifies the real application flow:

```text
HTTP Request
     |
     v
Controller
     |
     v
Reward Service
     |
     v
JPA Repository
     |
     v
H2 Database
     |
     v
Reward Calculation
     |
     v
HTTP Response
```

### Running Tests

```bash
mvn clean test
```

or:

```bash
mvn clean verify
```

Expected:

```text
Failures: 0
Errors: 0
BUILD SUCCESS
```

---

## Test Scenarios

### Reward Calculation Boundaries

| Amount | Expected Points |
|---:|---:|
| $49 | 0 |
| $50 | 0 |
| $51 | 1 |
| $99 | 49 |
| $100 | 50 |
| $101 | 52 |
| $120 | 90 |
| $150 | 150 |

### API Scenarios

| Scenario | HTTP Status |
|---|---:|
| Existing customer | 200 |
| Unknown customer | 404 |
| Negative customer ID | 400 |
| Non-numeric customer ID | 400 |

---

## Sample Data

The project includes sample customers and transactions for demonstrating the reward calculation.

### Customers

```text
1 - Anurag Ratna
2 - Manju Nath
3 - Suresh Kumar
```

Transactions cover multiple months and different reward calculation boundaries.

The seed data includes:

- Values below $50
- Exactly $50
- Values between $50 and $100
- Exactly $100
- Values above $100

---

## Configuration

Application configuration is stored in:

```text
src/main/resources/application.properties
```

Important configuration:

```properties
spring.application.name=customer-reward-points

spring.datasource.url=jdbc:h2:mem:rewarddb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### H2 Database

The database is in-memory.

Therefore:

- Data exists while the application is running.
- Restarting the application recreates the database.
- `schema.sql` recreates the tables.
- `data.sql` reloads the sample data.

### H2 Console

When the application is running:

```text
http://localhost:8080/h2-console
```

Connection details:

```text
JDBC URL: jdbc:h2:mem:rewarddb
Username: sa
Password: <blank>
```

The H2 console is intended for development and verification.

---

## Git Branching Strategy

The project uses feature branches and pull requests.

The main development branch is:

```text
develop
```

The final production branch is:

```text
main
```

Feature branches are created from `develop` and merged back through pull requests.

### Branch Structure

```text
main
 |
 +-- develop
      |
      |
      +-- feature/project-setup
      |
      +-- feature/database
      |
      +-- feature/implementation
      |
      +-- feature/testing
      |
      +-- feature/dockerization
      |
      +-- feature/documentation
```

This demonstrates incremental development and keeps major phases independently reviewable.

---

## Development Phases

### Repository Initialization

Implemented:
- Git repository
- `.gitignore`
- Initial README
- Initial branch structure

Branch:

```text
feature/develop
```

### Spring Boot Project Setup

Implemented:
- Spring Boot application
- Maven configuration
- Java 21
- Spring Web MVC
- Application startup verification

Branch:

```text
feature/project-setup
```

### Database and Domain Model

Implemented:
- Spring Data JPA
- H2 database
- SQL schema
- SQL seed data
- Customer entity
- Transaction entity
- Customer repository
- Transaction repository
- JPA relationship mappings
- Lombok

Branch:

```text
feature/database
```

### Reward Calculation

Implemented:
- Reward calculation business logic
- BigDecimal-based monetary calculations
- Reward calculation boundaries
- Amount validation

Branch:

```text
feature/implementation
```

### Reward Service

Implemented:
- Customer lookup
- Transaction retrieval
- Reward calculation orchestration
- Monthly aggregation
- Total reward calculation
- API DTOs

Branch:

```text
feature/implementation
```

### REST API

Implemented:
- Versioned REST API
- `GET /api/v1/rewards/{customerId}`
- Reward summary response
- REST controller

Branch:

```text
feature/implementation
```

### Exception Handling and Validation

Implemented:
- Customer-specific exception
- Global exception handling
- 404 customer-not-found response
- 400 invalid customer ID response
- Negative customer ID validation

Branch:

```text
feature/implementation
```

### Testing

Implemented:
- Reward calculation unit tests
- Reward service unit tests
- REST controller tests
- Integration tests
- Boundary tests
- Negative scenarios
- H2 integration testing

Branch:

```text
feature/testing
```

### Dockerization

Implemented:
- Dockerfile
- `.dockerignore`
- Docker image build
- Docker container execution
- API verification inside the container

Branch:

```text
feature/dockerization
```

### Documentation

Implemented:
- Complete project README
- Setup instructions
- API documentation
- Architecture documentation
- Testing documentation
- Docker instructions
- Git branching documentation
- Development phase documentation

Branch:

```text
feature/documentation
```

---

## Design Decisions

### Java 21

Java 21 LTS is used as the application runtime.

### Spring Boot 4.1.0

Spring Boot 4.1.0 is used with the corresponding Spring Boot 4 testing APIs.

### H2 Instead of an External Database

H2 was selected because the assignment does not require persistent external database infrastructure.

Benefits:

- No database installation
- No database server required
- Easy evaluator setup
- Fast tests
- Repeatable seed data
- Simple integration testing

### SQL Scripts

Database initialization is explicitly controlled using:

```text
schema.sql
data.sql
```

Hibernate automatic schema generation is disabled.

### BigDecimal for Monetary Values

Transaction amounts use:

```java
BigDecimal
```

instead of floating-point types such as `double`.

This avoids floating-point precision issues with monetary calculations.

### DTOs

JPA entities are not directly exposed through the REST API.

DTOs provide a separate API contract from the persistence model.

### API Versioning

The REST API uses URI-based versioning:

```text
/api/v1/rewards/{customerId}
```

This allows a future `/api/v2` without breaking existing v1 clients.

### Global Exception Handling

Application errors are converted into consistent REST responses through:

```text
GlobalExceptionHandler
```

### Layered Architecture

The project separates:

```text
Controller
Service
Business Logic
Repository
Entity
DTO
Exception Handling
```

This improves maintainability and testability.

---

## Important Notes

### H2 Is In-Memory

The database is recreated whenever the application restarts.

This is intentional for the assignment.

The application does not require:

- MySQL
- PostgreSQL
- External database installation
- Docker Compose

### Docker Is Optional

Docker is included as an additional deployment option.

The application can be evaluated using only:

```bash
mvn clean verify
mvn spring-boot:run
```

### Target Directory

The Maven `target/` directory is generated during builds and is excluded from Git.

---

## Final Verification

Before submission, run:

```bash
mvn clean verify
```

Expected:

```text
BUILD SUCCESS
```

Optional Docker verification:

```bash
mvn clean package
docker build -t retailer-rewards-program:1.0 .
docker run --name retailer-rewards-program -p 8080:8080 retailer-rewards-program:1.0
```

Then test:

```text
http://localhost:8080/api/v1/rewards/1
```

Expected:

```text
HTTP 200 OK
```

The project is designed so that an evaluator can run it without installing an external database.

---

## Author

Anurag Ratna

Built with:

```text
Java 21
Spring Boot 4.1.0
Spring Data JPA
H2
Maven
JUnit 5
Mockito
Docker
Git
```
