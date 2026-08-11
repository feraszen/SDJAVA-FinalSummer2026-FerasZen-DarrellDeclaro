-- Sample data for development and database testing.
-- User records will be created through the application's authentication workflow.

INSERT INTO memberships (membership_type, price)
VALUES
    ('Monthly', 45.00),
    ('Quarterly', 120.00),
    ('Annual', 420.00);

INSERT INTO gym_merch (product_name, type, price, current_stock)
VALUES
    ('Protein Shake', 'Food & Drink', 6.50, 25),
    ('Gym Water Bottle', 'Workout Gear', 15.00, 20),
    ('Resistance Band', 'Workout Gear', 12.00, 30),
    ('Protein Bar', 'Food & Drink', 3.50, 40);