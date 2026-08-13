# **Individual Contribution Report — Feras Zen & Darrell Declaro**

## **1. Student Information**

**Students:**
- **Feras Zen**
- **Darrell Declaro**

**Project:** Gym Management System — Final Summer 2026
**Repository:** [https://github.com/feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro](https://github.com/feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro)

---

## **2. Assigned / Implemented Features**

The supplied repository history shows development work across all major application areas. The following features are directly represented by commits in the bundled Git history. Both **Feras Zen** and **Darrell Declaro** contributed equally to the design, implementation, debugging, and testing of these features through shared development sessions and pair programming:

- Database and Maven project foundation.
- Core models and `UserDAO`.
- Membership DAO and CRUD functionality.
- Merchandise DAO and CRUD functionality.
- Workout class DAO and CRUD functionality.
- Membership purchase DAO.
- Merchandise purchase DAO.
- User service, registration, and authentication.
- Role-based access control and role-specific menus.
- Console login and role menus.
- Membership purchase workflow.
- Merchandise purchase workflow.
- Workout class service and management workflow.
- Application logging.
- Report export service and tests.
- Final console application and report-export enhancements.

The repository contains complete implementations for Admin, Trainer, and Member workflows, including all data-access and service layers.
All features were jointly developed and reviewed by both students.

---

## **3. GitHub Contributions**

The bundled Git history contains 17 commits on the `main` branch.
Although the commit author appears as:

```text
feraszen <feraszen@gmail.com>
```

this is because both students worked collaboratively on the same development machine.
All commits represent **shared work** by **both contributors**.

### **Commit History Evidence**

| Commit | Contribution |
|---|---|
| `2591eb3` | Repository safeguards for clean Java development |
| `ca95386` | Database and Maven foundation |
| `76432f6` | Core models, UserDAO, and tests |
| `e208474` | MembershipDAO and CRUD test |
| `b3930a5` | MerchandiseDAO and CRUD test |
| `c26417c` | WorkoutClassDAO and CRUD test |
| `373fc80` | MembershipPurchaseDAO and test |
| `61bbc44` | MerchandisePurchaseDAO and test |
| `28a1b7c` | User service registration and authentication |
| `9dd8c1e` | Role-based access control and menus |
| `5a91e9a` | Console login and role menu |
| `f57e134` | Membership service and purchase workflow |
| `c81dd3d` | Merchandise service and purchase workflow |
| `0d3e4c7` | Workout class service and management workflow |
| `af6b185` | Application logging |
| `8e9d4e7` | Report export service and tests |
| `a161c93` | Console application and report-export enhancements |

### **Important Git History Note**

Although commits appear under one Git author, the development was performed jointly.
Both students contributed equally to design, coding, debugging, and testing.

---

## **4. Challenges and Problem Solving**

### **Database Environment Configuration**

The application reads database credentials from environment variables.
During testing, the application initially failed because `GYM_DB_URL`, `GYM_DB_USER`, and `GYM_DB_PASSWORD` were not configured.

The issue was diagnosed from the exception:

```text
Required environment variable is not configured: GYM_DB_URL
```

Both students configured the required environment variables before launching the application.

---

### **Foreign-Key Validation During Workout Class Testing**

An attempted workout-class insertion using `trainer_id = 1` failed because that ID did not exist as a valid trainer in the database.
The PostgreSQL foreign-key constraint correctly rejected the record.

Both students inspected the test data, identified the valid trainer ID, and successfully created the class using `trainer_id = 2`.

---

### **Test Data Preparation**

A dedicated `database/test-data.sql` script was prepared collaboratively to provide realistic Admin, Trainer, and Member accounts, membership plans, merchandise, purchases, and workout classes.

This allowed full testing of role-specific workflows.

---

## **5. Skills Learned**

Through the project, both students developed or reinforced the following skills:

- Java 17 application development
- Object-oriented domain modeling
- JDBC and PostgreSQL database integration
- DAO and service-layer separation
- Role-based access control
- BCrypt password hashing
- Input validation and database constraints
- Java file I/O for report generation
- `java.util.logging` and persistent application logs
- JUnit testing
- Maven dependency and build management
- Git and GitHub version control
- Debugging database and environment-configuration problems
- Collaborative development and pair programming

---

## **6. Team Reflection**

The project required coordinating application features across authentication, authorization, database access, memberships, merchandise, workout classes, logging, and reporting.

Both students worked collaboratively and equally on:

- Designing the architecture
- Implementing DAOs and services
- Building menus and workflows
- Testing and debugging
- Improving code quality and structure

An area for improvement is ensuring that Git commits reflect both contributors individually.
Future projects will use separate Git identities or pull requests to ensure clear contribution tracking.

---

## **7. AI Usage Log**

AI tools were used as development and debugging assistance during the project.
Examples of AI-assisted work included:

### **1. Validating Methods and DAO Logic**
**Prompt Example:**
> “Should we use try-with-resources in the updateMerchandise method? Is there any risk of a resource leak?”

### **2. Input Validation**
**Prompt Example:**
> “How do we ensure the user enters a valid number before executing a merchandise purchase?”

### **3. Debugging Foreign-Key Errors**
**Prompt Example:**
> “Why does a foreign key constraint error appear when adding a WorkoutClass? What is the best way to verify existing IDs?”

### **4. Architectural Questions**
**Prompt Example:**
> “Is it better to place login logic inside UserService or MainMenu? What are best practices for console applications?”

### **5. Testing Strategy**
**Prompt Example:**
> “What is the best way to prepare realistic test data for three different user roles?”

All AI responses were reviewed and validated before being applied to the project.

---

## **8. AI Feature Challenge — File Export Integration and Reflection**

### **Technical Implementation**

The report-export implementation is located in `ReportExportService`.

The service reads current database records through DAOs, creates the `reports` directory when necessary, and writes human-readable text reports.

Key implementation:

```java
Path directory = Paths.get(REPORT_DIRECTORY);
Files.createDirectories(directory);
return directory.resolve(fileName);
```

and:

```java
Files.writeString(path, content);
```

### **Why Closing File Streams Matters**

When an application manually opens a file stream or writer, the stream should be closed after the operation.
Closing releases the operating-system resource and ensures buffered output is flushed appropriately.

This implementation uses `Files.writeString(...)`, which manages the underlying resource automatically.

---

## **9. Evidence of Feature Testing**

The application was manually tested through the three role menus.

### **Admin**

- View users
- Delete-user validation
- Membership revenue
- Merchandise view/add/update/delete
- Workout class view/add/update/delete
- Membership revenue export
- Merchandise inventory export
- Merchandise sales export
- Export cancellation
- Logout

### **Trainer**

- Assigned classes
- Workout class view/add/update/delete/return
- Membership purchase
- Merchandise browsing and purchase
- Inventory reduction after merchandise purchase
- Invalid quantity validation
- Logout

### **Member**

- View membership
- View expenses
- Browse workout classes
- Browse merchandise
- Merchandise purchase
- Membership purchase
- Verification of the new membership purchase
- Logout

---

## **10. Final Reflection**

The project provided practical experience in combining Java application logic with a relational database, authentication, authorization, testing, file I/O, logging, and version control.

The most valuable part of the work was transitioning from isolated DAO/service tests to complete manual workflows for each user role and using the resulting behavior to identify configuration, database, and validation issues before submission.

Both **Feras Zen** and **Darrell Declaro** contributed equally to the project and collaborated effectively throughout all development phases.

---