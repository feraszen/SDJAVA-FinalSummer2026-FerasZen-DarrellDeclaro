# Individual Contribution Report — Darrell Declaro

## 1. Student Information

- **Student:** Darrell Declaro
- **Project:** Gym Management System — Final Summer 2026
- **Repository:** `feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro`

<<<<<<< HEAD
## 2. Assigned / Implemented Features

The repository history documents collaborative development by Feras Zen and Darrell Declaro. The commits were created under the `feraszen` Git identity because the team worked collaboratively on the same development machine. Therefore, Git history does not provide reliable commit-by-commit attribution between the two students.

The shared implementation covered:
=======
## 2. Contribution Scope

The available Git history documents collaborative development by Darrell Declaro and Feras Zen. The commits were created under the `feraszen` Git identity because the team worked collaboratively on the same development machine. Therefore, the repository history does **not** provide reliable commit-by-commit attribution between the two students.

The existing team report states that both students contributed equally through shared development sessions and pair programming. This individual report preserves that evidence rather than inventing a separate attribution that Git cannot verify.

Shared work included:
>>>>>>> fix/final-review-priority

- Database and Maven project foundation.
- Core models and DAO implementations.
- Membership and membership-purchase workflows.
- Merchandise and merchandise-purchase workflows.
- Workout-class CRUD and management.
- User registration and authentication with BCrypt.
- Role-based access control and role-specific menus.
- Console application workflows.
- Application logging.
- Report export service and tests.
- Integration testing, debugging, and final documentation.
<<<<<<< HEAD
- Final compliance corrections for current-year membership revenue, inventory valuation, registration workflow, and DAO error logging.

This report intentionally does not assign individual commits to Darrell where the Git history cannot prove that attribution.

## 3. GitHub Contributions

The available Git history shows the major implementation commits under the shared `feraszen` identity, including database/Maven setup, models and DAOs, authentication, RBAC, membership, merchandise, workout classes, logging, report export, console refactoring, documentation, and final corrective work.

Because both students collaborated on the same development machine, the commit author alone is not reliable evidence of individual ownership. The contribution evidence should therefore be presented as collaborative work rather than as invented per-commit attribution.

## 4. Challenges & Problem Solving

### Database Environment Configuration

The application depends on PostgreSQL connection environment variables. During development, missing environment configuration prevented the application from connecting to the database. The issue was identified from the database configuration error and corrected by configuring the required environment values.

### Foreign-Key Validation

Workout-class testing exposed the requirement that `trainer_id` reference a valid trainer in the `users` table. The test data and valid trainer ID were checked before continuing the workflow.

### Trainer Class Ownership

The final review identified that a Trainer's assigned-class workflow needed to display and manage only classes belonging to the logged-in trainer. The console workflow was corrected to filter Trainer classes by the authenticated user's ID and to reject updates/deletions for another trainer's class.

### Final Compliance Corrections

The audit also identified that membership revenue had to be explicitly limited to the current calendar year, merchandise inventory needed per-item and total valuation, and DAO database errors needed persistent application logging instead of `printStackTrace()`. These were corrected in the final review branch.

## 5. Skills Learned
=======

## 3. GitHub Evidence

The repository history contains the major implementation commits, including `ca95386`, `76432f6`, `e208474`, `b3930a5`, `c26417c`, `373fc80`, `61bbc44`, `28a1b7c`, `9dd8c1e`, `5a91e9a`, `f57e134`, `c81dd3d`, `0d3e4c7`, `af6b185`, `8e9d4e7`, `a161c93`, `4e81403`, and `037fd59`.

The final review branch also contains shared corrective work for trainer-specific class filtering, annual revenue, inventory valuation, and DAO error logging.

## 4. Challenges and Problem Solving

### Database environment configuration

The application initially failed when the required database environment variables were missing. The problem was identified from the database configuration error and corrected by configuring the required environment values.

### Foreign-key validation

Workout-class testing exposed the requirement that `trainer_id` must reference a valid trainer in the `users` table. The test data was checked and the workflow was corrected to use valid foreign-key data.

### Final role-specific review

The final review identified that the Trainer's assigned-class menu was displaying all workout classes. The implementation was corrected so Trainer users retrieve and manage only classes assigned to their own user ID.

## 5. Skills Demonstrated
>>>>>>> fix/final-review-priority

- Java 17 and object-oriented programming
- JDBC and PostgreSQL
- DAO/service architecture
- Authentication and BCrypt password hashing
- Role-based authorization
- Console application design
- Input validation
- JUnit testing
- Maven build management
<<<<<<< HEAD
- Java NIO file I/O and report generation
=======
- Java file I/O and report generation
>>>>>>> fix/final-review-priority
- `java.util.logging`
- Git and GitHub collaboration
- Debugging and database troubleshooting

## 6. Team Reflection

The project was developed collaboratively. The strongest part of the process was integrating database access, business logic, authorization, console workflows, logging, reporting, and automated tests into one application.

<<<<<<< HEAD
A key improvement for future projects is to use separate Git identities or clearly attributed pull requests for each contributor so individual GitHub contribution evidence is easier to verify.

## 7. AI Usage Log

The existing combined project report documented several AI-assisted development examples, including questions about JDBC resource management, input validation, foreign-key errors, architecture, and testing.

Examples preserved from that report include:

> “Should we use try-with-resources in the updateMerchandise method? Is there any risk of a resource leak?”

> “How do we ensure the user enters a valid number before executing a merchandise purchase?”

> “Why does a foreign key constraint error appear when adding a WorkoutClass? What is the best way to verify existing IDs?”

> “Is it better to place login logic inside UserService or MainMenu? What are best practices for console applications?”

> “What is the best way to prepare realistic test data for three different user roles?”

**Verification note:** these prompts were present as examples in the existing repository report, but the exact original chat transcript was not available during this final audit. They are therefore not represented here as independently verified transcript evidence. Before submission, replace or supplement them with the exact prompts from the original conversation if the instructor requires verbatim AI dialogue.

## 8. Feature Challenge — File Export Reflection

The report-export feature reads current database records through the DAO/service layers, creates the `reports` directory when necessary, and writes human-readable `.txt` reports.

The implementation uses:

```java
Path directory = Paths.get(REPORT_DIRECTORY);
Files.createDirectories(directory);
return directory.resolve(fileName);
```

and:

```java
Files.writeString(path, content);
```

`Files.createDirectories(...)` ensures that the required output directory exists before writing. `Files.writeString(...)` handles the underlying file resource for the operation, avoiding a manually opened writer that could be left unclosed.

Closing manually opened streams is important because it releases operating-system resources and ensures buffered output is flushed. If a program terminates abnormally while output remains buffered, the report may be incomplete or missing data that had not yet reached disk.

**Code showcase:** the final `ReportExportService` creates `/reports/` automatically and writes the membership revenue, merchandise inventory, and merchandise sales reports as human-readable text files.

## 9. Testing Evidence

The final audit requires manual verification of Admin, Trainer, and Member workflows after all changes. The automated test suite is also expected to cover the current-year revenue and inventory valuation calculations in addition to the existing application tests.

Important final checks include:

1. Admin revenue is current-calendar-year only.
2. Admin inventory shows per-item valuation and total valuation.
3. Trainer sees only assigned classes.
4. Trainer cannot edit or delete another Trainer's class.
5. Admin can manage all workout classes.
6. Member can browse workout classes.
7. Database exceptions are logged through `AppLogger`.
8. Member registration is available without allowing self-registration as Admin or Trainer.
9. Reports are created under `/reports/`.

## 10. Final Reflection

This project provided practical experience combining Java application logic with PostgreSQL, authentication, authorization, testing, logging, file export, and GitHub-based development. The final audit reinforced the importance of validating the actual implementation against the exact assignment wording rather than relying only on the presence of a menu option or service method.
=======
An improvement for future projects is to use separate Git identities or clearly attributed pull requests for each contributor so individual GitHub contribution evidence is easier to verify.

## 7. AI Usage Log

AI assistance was used as a development and debugging aid for JDBC resource management, input validation, foreign-key troubleshooting, DAO/service separation, testing strategies, debugging, and final project review. AI suggestions were reviewed against the project code and requirements before being applied.

## 8. Feature Challenge Reflection — File Export

The report-export feature reads current database data through the DAO/service layers, creates the `reports` directory when needed, and writes human-readable text reports using Java NIO file APIs. The implementation uses `Files.createDirectories(...)` and `Files.writeString(...)`.

## 9. Testing Evidence

The application was tested through Admin, Trainer, and Member workflows, including authentication, role-specific menus, memberships, merchandise, workout classes, logging, and report export.

The final manual review must specifically verify that:

1. A Trainer sees only assigned classes.
2. A Trainer cannot edit or delete another Trainer's class.
3. Admin sees all workout classes.
4. Annual membership revenue is limited to the current calendar year.
5. Merchandise inventory shows per-item valuation and total valuation.
6. Database exceptions are written through `AppLogger` rather than `printStackTrace()`.

## 10. Final Reflection

This project provided practical experience combining Java application logic with PostgreSQL, authentication, authorization, testing, logging, file export, and GitHub-based development. The final review reinforced the importance of checking implemented behavior against the exact functional requirement.
>>>>>>> fix/final-review-priority
