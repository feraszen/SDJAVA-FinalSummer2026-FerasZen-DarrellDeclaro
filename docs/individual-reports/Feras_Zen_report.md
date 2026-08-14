# Individual Contribution Report — Feras Zen

## 1. Student Information

- **Student:** Feras Zen
- **Project:** Gym Management System — Final Summer 2026
- **Repository:** `feraszen/SDJAVA-FinalSummer2026-FerasZen-DarrellDeclaro`

## 2. Contribution Scope

The available Git history documents collaborative development by Feras Zen and Darrell Declaro. The commits were created under the `feraszen` Git identity because the team worked collaboratively on the same development machine. Therefore, the repository history does **not** provide reliable commit-by-commit attribution between the two students.

The existing team report states that both students contributed equally through shared development sessions and pair programming. This individual report preserves that evidence rather than inventing a separate attribution that Git cannot verify.

Shared work included:

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

## 3. GitHub Evidence

The repository history contains the major implementation commits, including:

- `ca95386` — database and Maven foundation
- `76432f6` — core models, UserDAO, and tests
- `e208474` — MembershipDAO and CRUD test
- `b3930a5` — MerchandiseDAO and CRUD test
- `c26417c` — WorkoutClassDAO and CRUD test
- `373fc80` — MembershipPurchaseDAO and test
- `61bbc44` — MerchandisePurchaseDAO and test
- `28a1b7c` — user service registration and authentication
- `9dd8c1e` — role-based access control and menus
- `5a91e9a` — console login and role menu
- `f57e134` — membership service and purchase workflow
- `c81dd3d` — merchandise service and purchase workflow
- `0d3e4c7` — workout class service and management workflow
- `af6b185` — application logging
- `8e9d4e7` — report export service and tests
- `a161c93` — console application and report-export enhancements
- `4e81403` — final project documentation and enhancements
- `037fd59` — log file ignore-rule correction

The final review branch also contains shared corrective work for trainer-specific class filtering, annual revenue, inventory valuation, and DAO error logging.

## 4. Challenges and Problem Solving

### Database environment configuration

The application initially failed when the required database environment variables were missing. The problem was identified from the database configuration error and corrected by configuring the required environment values.

### Foreign-key validation

Workout-class testing exposed the requirement that `trainer_id` must reference a valid trainer in the `users` table. The test data was checked and the workflow was corrected to use valid foreign-key data.

### Final role-specific review

The final review identified that the Trainer's assigned-class menu was displaying all workout classes. The implementation was corrected so Trainer users retrieve and manage only classes assigned to their own user ID.

## 5. Skills Demonstrated

- Java 17 and object-oriented programming
- JDBC and PostgreSQL
- DAO/service architecture
- Authentication and BCrypt password hashing
- Role-based authorization
- Console application design
- Input validation
- JUnit testing
- Maven build management
- Java file I/O and report generation
- `java.util.logging`
- Git and GitHub collaboration
- Debugging and database troubleshooting

## 6. Team Reflection

The project was developed collaboratively. The strongest part of the process was integrating database access, business logic, authorization, console workflows, logging, reporting, and automated tests into one application.

An improvement for future projects is to use separate Git identities or clearly attributed pull requests for each contributor so individual GitHub contribution evidence is easier to verify.

## 7. AI Usage Log

AI assistance was used as a development and debugging aid. Examples included questions about:

- JDBC resource management and try-with-resources.
- Input validation and safe console parsing.
- Foreign-key constraint errors.
- DAO/service separation.
- Testing strategies and realistic test data.
- Debugging and final project review.

AI-generated suggestions were reviewed against the project code and requirements before being applied.

## 8. Feature Challenge Reflection — File Export

The report-export feature reads current database data through the DAO/service layers, creates the `reports` directory when needed, and writes human-readable text reports using Java NIO file APIs.

The implementation uses `Files.createDirectories(...)` and `Files.writeString(...)`, which avoids leaving manually opened writer resources unclosed.

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

This project provided practical experience combining Java application logic with PostgreSQL, authentication, authorization, testing, logging, file export, and GitHub-based development. The final review also reinforced the importance of checking the implemented behavior against the exact functional requirement rather than relying only on the presence of a menu option or method.
