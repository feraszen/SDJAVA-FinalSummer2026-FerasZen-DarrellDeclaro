# Gym Management System

A console-based Gym Management System built with **Java 17**, **PostgreSQL**, **JDBC**, and **Maven**. The application provides role-based workflows for **Admins, Trainers, and Members**, including memberships, workout classes, merchandise inventory, purchases, authorization, logging, and report export.

> **Academic Project — Software Development / Java**

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![CI](https://github.com/feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro/actions/workflows/maven-ci.yml)

## Overview

The Gym Management System is designed as a layered Java console application backed by PostgreSQL. It separates console workflows, business logic, authorization, data access, and domain models to keep the application maintainable and testable.

The system supports three application roles:

| Role | Main Capabilities |
|---|---|
| **Admin** | Manage users, view current-year membership revenue, manage merchandise inventory, manage workout classes, and export reports |
| **Trainer** | View assigned classes, create/update/delete their own workout classes, purchase memberships, and purchase merchandise |
| **Member** | View memberships and expenses, browse workout classes and merchandise, and purchase memberships and merchandise |

## Key Features

### Authentication & Authorization

- Secure password hashing and verification using **BCrypt**.
- Role-based menus for `ADMIN`, `TRAINER`, and `MEMBER`.
- Explicit authorization checks at sensitive workflow boundaries.
- Self-service registration creates `MEMBER` accounts only.
- Failed login attempts and important application events are recorded through persistent logging.

### Membership Management

- View available membership plans.
- Purchase memberships as Members or Trainers.
- View personal membership purchase history.
- Track personal expenses.
- Admin membership revenue is calculated for the **current calendar year**.

### Workout Class Management

- Browse scheduled workout classes.
- Admins can manage all workout classes.
- Trainers can create, update, and delete classes assigned to themselves.
- Trainer update/delete operations are protected by ownership checks.
- Members can browse classes without management permissions.

### Merchandise & Inventory

- Browse merchandise and current stock.
- Admin CRUD operations for merchandise inventory.
- Purchase merchandise as Members or Trainers.
- Stock is reduced after successful purchases.
- Inventory valuation is calculated as:

```text
Inventory Value = Price × Current Stock
```

- Admin inventory views include per-item and total inventory valuation.

### Report Export

Admins can export human-readable text reports for:

1. Current-calendar-year membership revenue.
2. Merchandise inventory and valuation.
3. Merchandise sales.

The application creates the `reports/` directory automatically when reports are exported. Generated report files are runtime artifacts and do not need to be committed to the repository.

## Architecture

The project follows a layered architecture:

```text
┌──────────────────────────────┐
│       Console Layer          │
│ ConsoleApplication / Menus   │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│        Service Layer         │
│ Business Logic & Validation  │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          DAO Layer            │
│      JDBC / Data Access       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          PostgreSQL           │
│       gym_management_db       │
└──────────────────────────────┘
```

### Main Layers

- **Console:** Handles user interaction and role-specific workflows.
- **Service:** Contains application rules, validation, purchases, authorization, and reporting workflows.
- **DAO:** Isolates PostgreSQL/JDBC operations.
- **Model:** Represents users, memberships, purchases, merchandise, workout classes, and roles.
- **Config:** Provides database connection and application logging support.

## Project Structure

```text
.
├── .github/
│   └── workflows/
│       └── maven-ci.yml
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── test-data.sql
├── docs/
│   ├── developer_guide.md
│   ├── user_guide.md
│   └── individual-reports/
├── src/
│   ├── main/java/com/keyingym/
│   │   ├── ConsoleApplication.java
│   │   ├── config/
│   │   ├── console/
│   │   ├── dao/
│   │   ├── model/
│   │   └── service/
│   └── test/java/com/keyingym/
├── .gitignore
├── pom.xml
└── README.md
```

## Technology Stack

| Technology | Purpose |
|---|---|
| **Java 17** | Application language and runtime target |
| **PostgreSQL** | Relational database |
| **JDBC** | Database connectivity |
| **Maven** | Build and dependency management |
| **JUnit 5** | Automated testing |
| **jBCrypt** | Password hashing and verification |
| **java.util.logging** | Persistent application logging |
| **GitHub Actions** | Continuous integration and Maven verification |

The Maven configuration also uses the Maven Compiler, Surefire, and Shade plugins. The Shade plugin packages an executable JAR with the required runtime dependencies.

## Database

The application uses a PostgreSQL database named:

```text
gym_management_db
```

The database includes the main entities required by the application:

- `users`
- `memberships`
- `workout_classes`
- `gym_merch`
- `membership_purchases`
- `merchandise_purchases`

### Database Setup

Create the database first, then apply the schema:

```bash
psql -U postgres -d gym_management_db -f database/schema.sql
```

For development/demo data:

```bash
psql -U postgres -d gym_management_db -f database/test-data.sql
```

`database/seed.sql` provides base reference data.

## Configuration

Database credentials are supplied through local environment variables:

```text
GYM_DB_URL
GYM_DB_USER
GYM_DB_PASSWORD
```

Example configuration format:

```text
GYM_DB_URL=jdbc:postgresql://localhost:5432/gym_management_db
GYM_DB_USER=postgres
GYM_DB_PASSWORD=your_local_password
```

**Do not commit real credentials, passwords, or other secrets to GitHub.**

## Build & Run

### Prerequisites

- JDK 17 or later
- PostgreSQL
- Maven
- Git

### Build

From the project root:

```bash
mvn clean package
```

This command compiles the project, runs the automated tests, and creates the executable shaded JAR.

### Run

After a successful build:

```bash
java -jar target/gym-management-system-1.0-SNAPSHOT.jar
```

The application starts with the following main workflow:

```text
GYM MANAGEMENT SYSTEM

1. Login
2. Register as Member
3. Exit
```

## Testing

The project includes automated tests covering models, DAOs, services, authorization, role menus, report export, and console application behavior.

Run the test suite with:

```bash
mvn test
```

Or run the full verification/build process with:

```bash
mvn clean package
```

GitHub Actions also runs the Maven CI workflow for repository verification.

## Logging

The application uses `java.util.logging` with a persistent log file:

```text
app.log
```

Application events such as startup, failed login attempts, report exports, Admin overrides, and handled database transaction errors can be recorded for later review.

## Documentation

More detailed documentation is available in the repository:

- **[User Guide](docs/user_guide.md)** — application workflows, roles, memberships, classes, merchandise, reports, and limitations.
- **[Developer Guide](docs/developer_guide.md)** — architecture, class design, database design, setup, dependencies, logging, reporting, and testing.
- **[Database Schema](database/schema.sql)** — PostgreSQL database structure.
- **[Seed Data](database/seed.sql)** — base reference data.
- **[Test Data](database/test-data.sql)** — development and demonstration data.

## Security & Data Safety

This project is designed with several application-level security controls:

- BCrypt password hashing rather than storing plaintext passwords.
- Role-based authorization.
- Trainer ownership checks for workout-class updates and deletions.
- Restricted self-service registration.
- Environment-based database credentials.
- Sensitive local configuration excluded from version control.

This is an academic application and does not implement production payment processing. Purchases are recorded in PostgreSQL to simulate the purchasing workflow.

## Project Status

**Final academic project version — main branch.**

The final console refactor and compliance fixes have been merged into the repository's `main` branch. Continuous integration is configured through GitHub Actions.

## Contributors

- **Feras Zen**
- **Darrell Declaro**

## Repository

[View the project on GitHub](https://github.com/feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro)

---

Built as a Java software development project with a focus on clean architecture, role-based authorization, database integration, testing, logging, and maintainable console workflows.
