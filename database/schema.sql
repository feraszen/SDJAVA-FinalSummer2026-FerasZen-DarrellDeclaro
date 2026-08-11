-- Gym Management System database schema.
-- This script defines the tables and relationships required by the application.

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(25) NOT NULL,
    address VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT users_role_check
        CHECK (role IN ('ADMIN', 'TRAINER', 'MEMBER'))
);

-- Stores the membership plans available for purchase.
CREATE TABLE memberships (
    membership_id SERIAL PRIMARY KEY,
    membership_type VARCHAR(50) NOT NULL UNIQUE,
    price NUMERIC(10, 2) NOT NULL,
    CONSTRAINT memberships_price_check
        CHECK (price >= 0)
);

CREATE TABLE workout_classes (
    class_id SERIAL PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    trainer_id INTEGER NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    CONSTRAINT workout_classes_trainer_fk
        FOREIGN KEY (trainer_id) REFERENCES users(user_id)
);

CREATE TABLE gym_merch (
    merch_id SERIAL PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    current_stock INTEGER NOT NULL,
    CONSTRAINT gym_merch_price_check
        CHECK (price >= 0),
    CONSTRAINT gym_merch_stock_check
        CHECK (current_stock >= 0)
);

-- Records completed membership purchases so the application
-- can calculate individual expenses and overall membership revenue.
CREATE TABLE membership_purchases (
    purchase_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    membership_id INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT membership_purchases_user_fk
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT membership_purchases_membership_fk
        FOREIGN KEY (membership_id) REFERENCES memberships(membership_id),
    CONSTRAINT membership_purchases_price_check
        CHECK (price >= 0)
);

-- Records merchandise purchases so inventory and revenue can be tracked.
CREATE TABLE merchandise_purchases (
    purchase_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    merch_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT merchandise_purchases_user_fk
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT merchandise_purchases_merch_fk
        FOREIGN KEY (merch_id) REFERENCES gym_merch(merch_id),
    CONSTRAINT merchandise_purchases_quantity_check
        CHECK (quantity > 0),
    CONSTRAINT merchandise_purchases_price_check
        CHECK (unit_price >= 0)
);