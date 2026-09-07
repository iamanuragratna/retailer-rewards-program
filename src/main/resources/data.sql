-- Customers
INSERT INTO customers (id, name)
VALUES (1, 'Anurag Ratna');

INSERT INTO customers (id, name)
VALUES (2, 'Manju Nath');

INSERT INTO customers (id, name)
VALUES (3, 'Suresh Kumar');


-- Anurag Ratna - June 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (1, 1, 120.00, '2026-06-05');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (2, 1, 80.00, '2026-06-15');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (3, 1, 50.00, '2026-06-25');


-- Anurag Ratna - July 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (4, 1, 150.00, '2026-07-10');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (5, 1, 60.00, '2026-07-20');


-- Anurag Ratna - August 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (6, 1, 101.00, '2026-08-05');


-- Manju Nath - June 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (7, 2, 100.00, '2026-06-08');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (8, 2, 200.00, '2026-06-18');


-- Manju Nath - July 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (9, 2, 75.00, '2026-07-12');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (10, 2, 50.00, '2026-07-22');


-- Manju Nath - August 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (11, 2, 125.00, '2026-08-03');


-- Suresh Kumar - June 2026
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (12, 3, 40.00, '2026-06-07');

-- Boundary test values
INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (13, 3, 51.00, '2026-07-07');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (14, 3, 100.00, '2026-08-07');

INSERT INTO transactions (id, customer_id, amount, transaction_date)
VALUES (15, 3, 101.00, '2026-08-15');