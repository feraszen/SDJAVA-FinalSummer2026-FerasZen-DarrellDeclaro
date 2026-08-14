# Gym Management System — Developer Guide

## 1. Architecture Overview

The application follows a layered console architecture:

```text
ConsoleApplication / Console workflows
                |
                v
Service Layer
                |
                v
DAO Layer
                |
                v
JDBC / DatabaseConnection
                |
                v
PostgreSQL
```

The principal services are `UserService`, `MembershipService`, `MerchandiseService`, `WorkoutClassService`, `ReportExportService`, `AuthorizationService`, and `RoleMenuService`.

The principal DAOs are `UserDAO`, `MembershipDAO`, `MembershipPurchaseDAO`, `MerchandiseDAO`, `MerchandisePurchaseDAO`, and `WorkoutClassDAO`.

## 2. Project Structure

```text
src/main/java/com/keyingym/
  ConsoleApplication.java
  config/
  console/
  dao/
  model/
  service/

src/test/java/com/keyingym/
  ConsoleApplicationTest.java
  dao/
  model/
  service/

database/
  schema.sql
  seed.sql
  test-data.sql

docs/
  user_guide.md
  developer_guide.md
  individual-reports/

pom.xml
```

Generated report files are runtime artifacts. The application creates the `reports/` directory when an export is requested; the generated `.txt` files are not required to be committed to the repository.

## 3. Class Design

### Models

- `User` — application user.
- `UserRole` — `ADMIN`, `TRAINER`, and `MEMBER` roles.
- `Membership` — membership plan.
- `MembershipPurchase` — completed membership purchase.
- `Merchandise` — inventory item, including `getInventoryValue()` for price × current stock.
- `MerchandisePurchase` — merchandise purchase.
- `WorkoutClass` — scheduled workout class.

### Console Layer

The console layer contains focused workflows for Admin, Member, Merchandise, and Workout Class operations. `ConsoleApplication` handles startup, registration, authentication, navigation, and report export dispatch.

### DAO Layer

DAOs isolate PostgreSQL/JDBC operations. Database exceptions are recorded through `AppLogger.error(...)` rather than `printStackTrace()`.

`MembershipPurchaseDAO.getCurrentYearMembershipPurchases()` queries only purchases between the start of the current calendar year and the start of the next year.

### Service Layer

- `UserService` manages registration, authentication, user lookup, and deletion.
- `MembershipService` manages plans, purchases, personal purchase data, and current-year revenue data.
- `MerchandiseService` manages inventory CRUD and purchase workflows.
- `WorkoutClassService` manages class CRUD and trainer ownership rules.
- `AuthorizationService` defines role permissions.
- `RoleMenuService` builds role-specific menus.
- `ReportExportService` creates the required human-readable reports.

## 4. Role-Based Access Control

The visible menus are role-specific, and sensitive console workflows also perform role checks at their boundaries.

Self-service registration always creates a `MEMBER` account. A registering user cannot select `ADMIN` or `TRAINER`.

Trainer workout-class workflows are restricted to the logged-in trainer's classes for update and delete operations.

Merchandise purchase workflow boundaries accept only `MEMBER` and `TRAINER` users.

## 5. Database Design

The database is PostgreSQL and is accessed through JDBC.

### Tables

- `users`
- `memberships`
- `workout_classes`
- `gym_merch`
- `membership_purchases`
- `merchandise_purchases`

Primary keys, foreign keys, role constraints, non-negative price/stock constraints, and positive purchase-quantity constraints are defined in `database/schema.sql`.

`database/seed.sql` provides base reference data. `database/test-data.sql` provides full demonstration/test data including Admin, Trainer, and Member records, memberships, purchases, merchandise, and workout classes.

## 6. Setup

Requirements:

- JDK 17 or later.
- PostgreSQL.
- Maven.
- Git.

Create the PostgreSQL database and apply `database/schema.sql`, then load `database/test-data.sql` when demonstration data is required.

Configure the local database connection through:

```text
GYM_DB_URL
GYM_DB_USER
GYM_DB_PASSWORD
```

Do not commit real database credentials to the repository.

## 7. Maven Build and Run

Build the project with:

```bash
mvn clean package
```

The Maven Shade Plugin creates an executable JAR containing the application dependencies. The verified application run command is:

```bash
java -jar target/gym-management-system-1.0-SNAPSHOT.jar
```

The older classpath example using `target/dependency/*` is not the primary run instruction because the current Maven configuration does not copy dependencies to that directory.

## 8. Dependencies

The project uses:

- PostgreSQL JDBC driver `42.7.13`.
- jBCrypt `0.4`.
- JUnit Jupiter `5.13.4`.
- Maven Compiler Plugin `3.14.1` with Java release 17.
- Maven Surefire Plugin `3.5.4`.
- Maven Shade Plugin `3.6.0`.

## 9. Logging

`AppLogger` uses `java.util.logging` and writes persistent records to `app.log`.

Logged events include system startup, failed login attempts, report exports, Admin override actions, and database transaction errors handled by DAO classes.

## 10. File Export Challenge

`ReportExportService` implements the Admin export challenge.

The service:

1. Reads current database records through DAOs.
2. Uses `Files.createDirectories(...)` to create `reports/` when necessary.
3. Writes human-readable text using `Files.writeString(...)`.
4. Logs successful exports.

The membership revenue report uses the current-calendar-year definition. The inventory report includes per-item inventory value and total inventory valuation.

The implementation uses Java NIO convenience APIs instead of leaving manually opened writer resources unclosed. If manually opened streams are used elsewhere, they must be closed so resources are released and buffered output is flushed.

## 11. Testing

The repository contains JUnit tests for the console application, DAOs, models, authorization, services, menus, and report export.

The final audit added coverage for:

- Member registration workflow.
- Current-year membership revenue service data.
- Current-year membership revenue report export.
- Per-item merchandise inventory valuation.
- Total merchandise inventory valuation.

A full final Maven run should be performed locally after pulling the final branch. The previous verified baseline was 42 tests with zero failures/errors; the current branch contains additional tests and therefore should be expected to exceed that baseline.
