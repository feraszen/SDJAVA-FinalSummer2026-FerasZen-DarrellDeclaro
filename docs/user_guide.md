# Gym Management System — User Guide

## 1. System Overview

The Gym Management System is a console-based Java application for managing gym operations through Admin, Trainer, and Member roles.

The main functions are:

- Member registration and authentication.
- Role-based menus.
- Membership purchasing and expense tracking.
- Current-calendar-year membership revenue tracking for Admin users.
- Workout class scheduling and browsing.
<<<<<<< HEAD
- Merchandise inventory, valuation, and purchasing.
- Text report export for Admin users.
=======
- Merchandise inventory and purchasing.
- Current-calendar-year membership revenue tracking for administrators.
- Merchandise inventory valuation for administrators.
- Exporting reports to text files for administrators.
>>>>>>> fix/final-review-priority

## 2. User Roles

### Admin

- View all users and contact information.
- Delete users.
<<<<<<< HEAD
- View current-calendar-year membership revenue.
- Manage merchandise inventory and view per-item and total inventory valuation.
- Manage workout classes.
=======
- View membership revenue for the current calendar year.
- Manage merchandise inventory and view total inventory valuation.
- Manage all workout classes.
>>>>>>> fix/final-review-priority
- Export reports.
- Log out.

### Trainer

<<<<<<< HEAD
- View assigned workout classes.
- Create, update, and delete workout classes.
=======
A Trainer can manage workout classes assigned to that Trainer and purchase gym products or memberships.

Trainer functions include:

- View assigned workout classes only.
- Create workout classes assigned to the logged-in Trainer.
- Update or delete classes assigned to the logged-in Trainer.
>>>>>>> fix/final-review-priority
- Purchase a gym membership.
- Browse and purchase merchandise.
- Log out.

A Trainer cannot update or delete a class assigned to another Trainer.

### Member

- View personal membership purchases.
- View personal expenses.
- Browse available workout classes.
- Browse merchandise.
- Purchase a gym membership.
- Purchase merchandise.
- Log out.

## 3. Registration and Login

### 3.1 Registering a Member

1. Start the application.
2. Select **Register as Member**.
3. Enter username, password, email, phone number, and address.
4. The application creates the account with the `MEMBER` role.
5. Select **Login** from the start menu and use the new credentials.

Self-service registration does not allow a user to choose `ADMIN` or `TRAINER`.

### 3.2 Logging In

1. Select **Login**.
2. Enter the username.
3. Enter the password.
4. The application displays the menu allowed for the authenticated role.

Failed login attempts are recorded in the persistent application log.

## 4. Memberships

Trainers and Members can purchase memberships through **Purchase Membership**.

Admin revenue is calculated from membership purchases made during the **current calendar year only**. Earlier purchases are excluded from the current-year total.

## 5. Workout Classes

Admins and Trainers can use **Manage Workout Classes** to view, add, update, and delete classes.

- Trainers are associated with their own user ID when creating or updating classes.
- Trainers can update or delete only their own assigned classes.
- Admins can manage all workout classes.
- Members can use **Browse Workout Classes** without management permissions.

<<<<<<< HEAD
## 6. Merchandise
=======
**Admin:**

1. Open **Manage Workout Classes**.
2. Choose View, Add, Update, Delete, or Return.
3. Admins can manage classes for any valid trainer.

**Trainer:**

1. Open **View Assigned Classes** to see only classes assigned to the logged-in Trainer.
2. Open **Manage Workout Classes** to view and manage the Trainer's own assigned classes.
3. When creating a class, the logged-in Trainer is automatically used as the trainer assignment.
4. A Trainer cannot update or delete another Trainer's class.

When creating or updating a class, the system validates the class name, trainer relationship, and scheduled date/time.
>>>>>>> fix/final-review-priority

Admins can use **Manage Merchandise Inventory** to view, add, update, and delete merchandise.

Admin inventory displays:

- Product price.
- Current stock.
- Per-item inventory value.
- Total inventory valuation.

The calculation is:

<<<<<<< HEAD
```text
Inventory Value = Price × Current Stock
```
=======
1. Open **Manage Merchandise Inventory**.
2. View current inventory or choose Add, Update, or Delete.
3. Each item shows price, current stock, and inventory valuation calculated as `price × current stock`.
4. The bottom of the inventory view shows **Total Inventory Valuation**.
5. For new merchandise, enter the product name, type, price, and current stock.
6. For an update, enter the merchandise ID and the new information.
7. For deletion, enter the merchandise ID and confirm the deletion.
>>>>>>> fix/final-review-priority

The total valuation is the sum of the current inventory values of all merchandise items.

Trainers and Members can browse and purchase merchandise. Successful purchases reduce available stock.

<<<<<<< HEAD
## 7. Report Export
=======
### 3.5 Viewing Membership Revenue

Available only to Admins.

The **View Membership Revenue** option reports purchases made during the current calendar year. The report displays the qualifying purchases and the total membership revenue for that year.

### 3.6 Exporting Reports
>>>>>>> fix/final-review-priority

Only Admin users can export reports.

Available reports:

1. Membership Revenue Report — current calendar year.
2. Merchandise Inventory Report — includes per-item and total inventory valuation.
3. Merchandise Sales Report.

The application creates the `reports` directory automatically when necessary and writes human-readable `.txt` files.

Generated reports are runtime artifacts and do not need to exist in the repository before export is performed.

## 8. System Limitations

- The interface is console-based.
- Payment processing is simulated by recording purchases in PostgreSQL.
- PostgreSQL must be available before database workflows can operate.
- Database connection settings are supplied through the local environment.
- Reports are plain-text files rather than PDF or spreadsheet files.

## 9. Data Safety

<<<<<<< HEAD
Do not commit real database credentials or other secrets to GitHub. Use local environment configuration for database access.

The SQL test-data script provides sample data for development and demonstration.
=======
The project test database scripts provide sample data for development and demonstration.

Database errors are recorded through the application's persistent `AppLogger` instead of relying on console stack traces.
>>>>>>> fix/final-review-priority
