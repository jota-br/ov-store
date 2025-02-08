INSERT INTO countries (name)
VALUES ('United States'), ('Canada');

INSERT INTO states (name, country_id)
VALUES ('California', 1), ('Ontario', 2);

INSERT INTO cities (name, state_id)
VALUES ('Los Angeles', 1), ('Toronto', 2);

INSERT INTO zip_codes (zip_code, city_id)
VALUES ('90001', 1), ('M5H', 2);

INSERT INTO streets (name, zip_code_id, city_id)
VALUES ('Sunset Boulevard', 1, 1), ('Queen Street', 2, 2);

INSERT INTO users (username, salt, hash, email, first_name, last_name, phone, is_active)
VALUES
('john_doe', 'random_salt', 'hashed_password', 'john.doe@example.com', 'John', 'Doe', '+11234567890', TRUE),
('jane_smith', 'random_salt2', 'hashed_password2', 'jane.smith@example.com', 'Jane', 'Smith', '+11234567891', TRUE);

INSERT INTO addresses (user_id, street_id, address_number, address_type)
VALUES
(1, 1, '101', 'home'),
(2, 2, '202B', 'work');

INSERT INTO roles (name, description)
VALUES ('Admin', 'Administrator Role'), ('Customer', 'Customer Role');

INSERT INTO permissions (name, description)
VALUES ('VIEW_PRODUCTS', 'Can view products'), ('EDIT_PRODUCTS', 'Can edit products');

INSERT INTO role_permissions (role_id, permission_id)
VALUES
(1, 1), -- Admin can view products
(1, 2), -- Admin can edit products
(2, 1); -- Customer can view products

INSERT INTO user_roles (user_id, role_id)
VALUES
(1, 1), -- John Doe is Admin
(2, 2); -- Jane Smith is Customer

INSERT INTO categories (name, description, is_active)
VALUES ('Electronics', 'Electronic Items', TRUE), ('Books', 'Books and Literature', TRUE);

INSERT INTO products (name, description, price, stock, is_active)
VALUES
('Smartphone', 'Latest model smartphone', 699.99, 50, TRUE),
('Novel Book', 'Bestselling novel', 19.99, 100, TRUE);

INSERT INTO product_category (product_id, category_id)
VALUES
(1, 1), -- Smartphone in Electronics
(2, 2); -- Novel Book in Books

INSERT INTO product_images (product_id, image_url, is_main)
VALUES
(1, 'http://example.com/images/smartphone.jpg', TRUE),
(2, 'http://example.com/images/novel.jpg', TRUE);

INSERT INTO carts (user_id)
VALUES (2); -- Jane Smith's cart

INSERT INTO cart_items (cart_id, product_id, quantity)
VALUES
(1, 2, 1); -- Jane Smith added 1 Novel Book to cart

INSERT INTO orders (user_id, total_amount, status, shipping_address_id, billing_address_id)
VALUES
(2, 19.99, 'pending', 2, 2); -- Jane Smith placed an order

INSERT INTO order_details (order_id, product_id, quantity, unit_price)
VALUES
(1, 2, 1, 19.99); -- Order includes 1 Novel Book

INSERT INTO payments (order_id, amount, payment_method, status)
VALUES
(1, 19.99, 'Credit Card', 'completed');

INSERT INTO shippings (order_id, shipping_method, carrier, tracking_number, status)
VALUES
(1, 'Standard', 'UPS', '1Z999AA10123456784', 'shipped');

INSERT INTO order_status_history (order_id, status)
VALUES
(1, 'pending'),
(1, 'processed'),
(1, 'shipped');

INSERT INTO coupons (code, description, discount_type, discount_value, expiration_date, usage_limit)
VALUES
('WELCOME10', '10% off first order', 'percentage', 10.00, '2025-12-31', 1000);

INSERT INTO order_coupons (order_id, coupon_id)
VALUES
(1, 1); -- Jane used the WELCOME10 coupon

INSERT INTO email_verification_tokens (user_id, token, expires_at)
VALUES
(1, 'verification_token_123', '2024-01-01'),
(2, 'verification_token_456', '2024-01-01');

INSERT INTO password_reset_tokens (user_id, token, expires_at)
VALUES
(1, 'reset_token_123', '2023-12-31'),
(2, 'reset_token_456', '2023-12-31');

INSERT INTO user_audit (user_id, action, changed_data, changed_by)
VALUES
(1, 'CREATE', 'Created user account', 1),
(2, 'CREATE', 'Created user account', 2);


SELECT
  u.first_name,
  u.last_name,
  a.address_number,
  s.name AS street_name,
  c.name AS city_name,
  st.name AS state_name,
  z.zip_code,
  co.name AS country_name
FROM
  users u
JOIN addresses a ON u.user_id = a.user_id
JOIN streets s ON a.street_id = s.street_id
JOIN zip_codes z ON s.zip_code_id = z.zip_code_id
JOIN cities c ON s.city_id = c.city_id
JOIN states st ON c.state_id = st.state_id
JOIN countries co ON st.country_id = co.country_id;
