-- ============================================================
-- GYM MANAGEMENT SYSTEM
-- TEST DATA / SEED SCRIPT
-- ============================================================

-- Enable PostgreSQL cryptographic functions.
CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- ============================================================
-- USERS
-- ============================================================

-- Admin
INSERT INTO users
    (username, password, email, phone, address, role)
SELECT
    'admin',
    crypt('GymAdmin@2026', gen_salt('bf')),
    'admin@gym.com',
    '709-555-0001',
    'Gym Management Office',
    'ADMIN'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'admin'
);


-- Trainer
INSERT INTO users
    (username, password, email, phone, address, role)
SELECT
    'trainer1',
    crypt('Trainer@2026', gen_salt('bf')),
    'trainer1@gym.com',
    '709-555-0002',
    'Gym Training Department',
    'TRAINER'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'trainer1'
);


-- Member 1
INSERT INTO users
    (username, password, email, phone, address, role)
SELECT
    'member1',
    crypt('Member@2026', gen_salt('bf')),
    'member1@gym.com',
    '709-555-0003',
    'St. John''s, NL',
    'MEMBER'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'member1'
);


-- Member 2
INSERT INTO users
    (username, password, email, phone, address, role)
SELECT
    'member2',
    crypt('Member2@2026', gen_salt('bf')),
    'member2@gym.com',
    '709-555-0004',
    'St. John''s, NL',
    'MEMBER'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'member2'
);


-- ============================================================
-- MEMBERSHIPS
-- ============================================================

INSERT INTO memberships
    (membership_type, price)
SELECT
    'Monthly',
    49.99
WHERE NOT EXISTS (
    SELECT 1
    FROM memberships
    WHERE membership_type = 'Monthly'
);


INSERT INTO memberships
    (membership_type, price)
SELECT
    'Quarterly',
    129.99
WHERE NOT EXISTS (
    SELECT 1
    FROM memberships
    WHERE membership_type = 'Quarterly'
);


INSERT INTO memberships
    (membership_type, price)
SELECT
    'Annual',
    449.99
WHERE NOT EXISTS (
    SELECT 1
    FROM memberships
    WHERE membership_type = 'Annual'
);


-- ============================================================
-- MEMBERSHIP PURCHASES
-- ============================================================

INSERT INTO membership_purchases
    (user_id, membership_id, price, purchased_at)
SELECT
    u.user_id,
    m.membership_id,
    m.price,
    CURRENT_TIMESTAMP - INTERVAL '10 days'
FROM users u
JOIN memberships m
    ON m.membership_type = 'Monthly'
WHERE u.username = 'member1'
  AND NOT EXISTS (
      SELECT 1
      FROM membership_purchases mp
      WHERE mp.user_id = u.user_id
        AND mp.membership_id = m.membership_id
  );


INSERT INTO membership_purchases
    (user_id, membership_id, price, purchased_at)
SELECT
    u.user_id,
    m.membership_id,
    m.price,
    CURRENT_TIMESTAMP - INTERVAL '5 days'
FROM users u
JOIN memberships m
    ON m.membership_type = 'Annual'
WHERE u.username = 'member2'
  AND NOT EXISTS (
      SELECT 1
      FROM membership_purchases mp
      WHERE mp.user_id = u.user_id
        AND mp.membership_id = m.membership_id
  );


-- ============================================================
-- MERCHANDISE
-- ============================================================

INSERT INTO gym_merch
    (product_name, type, price, current_stock)
SELECT
    'Protein Shake',
    'Food & Drink',
    6.50,
    25
WHERE NOT EXISTS (
    SELECT 1
    FROM gym_merch
    WHERE product_name = 'Protein Shake'
);


INSERT INTO gym_merch
    (product_name, type, price, current_stock)
SELECT
    'Gym Water Bottle',
    'Workout Gear',
    15.00,
    20
WHERE NOT EXISTS (
    SELECT 1
    FROM gym_merch
    WHERE product_name = 'Gym Water Bottle'
);


INSERT INTO gym_merch
    (product_name, type, price, current_stock)
SELECT
    'Resistance Band',
    'Workout Gear',
    12.00,
    30
WHERE NOT EXISTS (
    SELECT 1
    FROM gym_merch
    WHERE product_name = 'Resistance Band'
);


INSERT INTO gym_merch
    (product_name, type, price, current_stock)
SELECT
    'Protein Bar',
    'Food & Drink',
    3.50,
    40
WHERE NOT EXISTS (
    SELECT 1
    FROM gym_merch
    WHERE product_name = 'Protein Bar'
);


INSERT INTO gym_merch
    (product_name, type, price, current_stock)
SELECT
    'Gym T-Shirt',
    'Workout Gear',
    25.00,
    15
WHERE NOT EXISTS (
    SELECT 1
    FROM gym_merch
    WHERE product_name = 'Gym T-Shirt'
);


-- ============================================================
-- MERCHANDISE PURCHASES
-- ============================================================

INSERT INTO merchandise_purchases
    (user_id, merch_id, quantity, unit_price, purchased_at)
SELECT
    u.user_id,
    m.merch_id,
    2,
    m.price,
    CURRENT_TIMESTAMP - INTERVAL '3 days'
FROM users u
JOIN gym_merch m
    ON m.product_name = 'Protein Shake'
WHERE u.username = 'member1'
  AND NOT EXISTS (
      SELECT 1
      FROM merchandise_purchases mp
      WHERE mp.user_id = u.user_id
        AND mp.merch_id = m.merch_id
  );


INSERT INTO merchandise_purchases
    (user_id, merch_id, quantity, unit_price, purchased_at)
SELECT
    u.user_id,
    m.merch_id,
    1,
    m.price,
    CURRENT_TIMESTAMP - INTERVAL '2 days'
FROM users u
JOIN gym_merch m
    ON m.product_name = 'Gym Water Bottle'
WHERE u.username = 'member2'
  AND NOT EXISTS (
      SELECT 1
      FROM merchandise_purchases mp
      WHERE mp.user_id = u.user_id
        AND mp.merch_id = m.merch_id
  );


-- ============================================================
-- WORKOUT CLASSES
-- ============================================================

INSERT INTO workout_classes
    (class_name, description, trainer_id, scheduled_at)
SELECT
    'Beginner Yoga',
    'Beginner-friendly yoga class.',
    u.user_id,
    CURRENT_TIMESTAMP + INTERVAL '2 days'
FROM users u
WHERE u.username = 'trainer1'
  AND u.role = 'TRAINER'
  AND NOT EXISTS (
      SELECT 1
      FROM workout_classes wc
      WHERE wc.class_name = 'Beginner Yoga'
  );


INSERT INTO workout_classes
    (class_name, description, trainer_id, scheduled_at)
SELECT
    'Strength Training',
    'Full-body strength training session.',
    u.user_id,
    CURRENT_TIMESTAMP + INTERVAL '4 days'
FROM users u
WHERE u.username = 'trainer1'
  AND u.role = 'TRAINER'
  AND NOT EXISTS (
      SELECT 1
      FROM workout_classes wc
      WHERE wc.class_name = 'Strength Training'
  );


INSERT INTO workout_classes
    (class_name, description, trainer_id, scheduled_at)
SELECT
    'HIIT Training',
    'High-intensity interval training class.',
    u.user_id,
    CURRENT_TIMESTAMP + INTERVAL '6 days'
FROM users u
WHERE u.username = 'trainer1'
  AND u.role = 'TRAINER'
  AND NOT EXISTS (
      SELECT 1
      FROM workout_classes wc
      WHERE wc.class_name = 'HIIT Training'
  );


-- ============================================================
-- VERIFICATION
-- ============================================================

SELECT
    user_id,
    username,
    email,
    role
FROM users
ORDER BY user_id;


SELECT
    membership_id,
    membership_type,
    price
FROM memberships
ORDER BY membership_id;


SELECT
    merch_id,
    product_name,
    type,
    price,
    current_stock
FROM gym_merch
ORDER BY merch_id;


SELECT
    class_id,
    class_name,
    trainer_id,
    scheduled_at
FROM workout_classes
ORDER BY scheduled_at;


SELECT
    purchase_id,
    user_id,
    membership_id,
    price,
    purchased_at
FROM membership_purchases
ORDER BY purchase_id;


SELECT
    purchase_id,
    user_id,
    merch_id,
    quantity,
    unit_price,
    purchased_at
FROM merchandise_purchases
ORDER BY purchase_id;
