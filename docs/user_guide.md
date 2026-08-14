# Gym Management System — User Guide

## 1. System Overview

The Gym Management System is a console-based Java application for managing gym operations through Admin, Trainer, and Member roles.

The main functions are:

- Member registration and authentication.
- Role-based menus.
- Membership purchasing and expense tracking.
- Current-calendar-year membership revenue tracking for Admin users.
- Workout class scheduling and browsing.
- Merchandise inventory, valuation, and purchasing.
- Text report export for Admin users.

## 2. User Roles

### Admin

- View all users and contact information.
- Delete users.
- View current-calendar-year membership revenue.
- Manage merchandise inventory and view per-item and total inventory valuation.
- Manage workout classes.
- Export reports.
- Log out.

### Trainer

- View assigned workout classes.
- Create, update, and delete workout classes.
- Purchase a gym membership.
- Browse and purchase merchandise.
- Log out.

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

## 6. Merchandise

Admins can use **Manage Merchandise Inventory** to view, add, update, and delete merchandise.

Admin inventory displays:

- Product price.
- Current stock.
- Per-item inventory value.
- Total inventory valuation.

The calculation is:

```text
Inventory Value = Price × Current Stock
```

The total valuation is the sum of the current inventory values of all merchandise items.

Trainers and Members can browse and purchase merchandise. Successful purchases reduce available stock.

## 7. Report Export

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

Do not commit real database credentials or other secrets to GitHub. Use local environment configuration for database access.

The SQL test-data script provides sample data for development and demonstration.
