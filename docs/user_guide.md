# Gym Management System — User Guide

## 1. System Overview

The Gym Management System is a console-based Java application for managing common gym operations through three role-specific accounts: Admin, Trainer, and Member.

The system connects to a local PostgreSQL database and provides menus that are restricted according to the logged-in user's role.

The main functions are:

- User authentication and role-based menus.
- Membership purchasing and expense tracking.
- Workout class scheduling and browsing.
- Merchandise inventory and purchasing.
- Current-calendar-year membership revenue tracking for administrators.
- Merchandise inventory valuation for administrators.
- Exporting reports to text files for administrators.

## 2. User Roles

### Admin

An Admin manages the gym's operational data.

Admin functions include:

- View all users and contact information.
- Delete users.
- View membership revenue for the current calendar year.
- Manage merchandise inventory and view total inventory valuation.
- Manage all workout classes.
- Export reports.
- Log out.

### Trainer

A Trainer can manage workout classes assigned to that Trainer and purchase gym products or memberships.

Trainer functions include:

- View assigned workout classes only.
- Create workout classes assigned to the logged-in Trainer.
- Update or delete classes assigned to the logged-in Trainer.
- Purchase a gym membership.
- Browse and purchase merchandise.
- Log out.

A Trainer cannot update or delete a class assigned to another Trainer.

### Member

A Member can view personal information related to memberships and purchases and can browse or purchase gym products.

Member functions include:

- View personal membership purchases.
- View personal expenses.
- Browse available workout classes.
- Browse merchandise.
- Purchase a gym membership.
- Purchase merchandise.
- Log out.

## 3. Common Workflows

### 3.1 Logging In

1. Start the application.
2. Enter the username.
3. Enter the password.
4. After successful authentication, the system displays the menu allowed for the user's role.

If authentication fails, the application rejects the login attempt and records the failed attempt in `app.log`.

### 3.2 Purchasing a Membership

Available to Trainers and Members.

1. Log in.
2. Select **Purchase Membership**.
3. Review the available membership plans and prices.
4. Enter the membership ID.
5. The system records the purchase and displays a success message.

Members can later view their membership purchases and expenses.

### 3.3 Managing Workout Classes

Available to Admins and Trainers.

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

Members can use **Browse Workout Classes** to view available classes without managing them.

### 3.4 Managing Merchandise

Available to Admins for inventory management.

Admin workflow:

1. Open **Manage Merchandise Inventory**.
2. View current inventory or choose Add, Update, or Delete.
3. Each item shows price, current stock, and inventory valuation calculated as `price × current stock`.
4. The bottom of the inventory view shows **Total Inventory Valuation**.
5. For new merchandise, enter the product name, type, price, and current stock.
6. For an update, enter the merchandise ID and the new information.
7. For deletion, enter the merchandise ID and confirm the deletion.

Trainers and Members can browse merchandise and purchase available products.

When merchandise is purchased, the quantity is recorded and the available stock is reduced.

### 3.5 Viewing Membership Revenue

Available only to Admins.

The **View Membership Revenue** option reports purchases made during the current calendar year. The report displays the qualifying purchases and the total membership revenue for that year.

### 3.6 Exporting Reports

Available only to Admins.

1. Open **Export Reports**.
2. Choose a report type:
   - Membership Revenue Report
   - Merchandise Inventory Report
   - Merchandise Sales Report
3. The application creates the `reports` directory when necessary.
4. The selected report is written as a human-readable `.txt` file.

The current implementation generates:

- `reports/membership-revenue-report.txt`
- `reports/merchandise-inventory-report.txt`
- `reports/merchandise-sales-report.txt`

## 4. System Limitations

- The user interface is console-based rather than graphical.
- Payment processing is not connected to a real payment provider; purchases are recorded directly by the application.
- The database must be available locally before the application can authenticate users or perform database operations.
- Database credentials are supplied through environment variables and are not stored in the Java source code.
- Report files are plain text files rather than PDF or spreadsheet reports.
- The system is intended for the course project environment and does not provide production-grade deployment, payment, or multi-site features.

## 5. Data Safety and Good Practice

Do not commit real database passwords to GitHub. Configure the required database environment variables locally before running the application.

The project test database scripts provide sample data for development and demonstration.

Database errors are recorded through the application's persistent `AppLogger` instead of relying on console stack traces.
