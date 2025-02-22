DELETE FROM wishlist_items;
DELETE FROM wishlists;
DELETE FROM reviews;
DELETE FROM order_status_history;
DELETE FROM shippings;
DELETE FROM payments;
DELETE FROM cart_items;
DELETE FROM carts;
DELETE FROM order_coupons;
DELETE FROM coupons;
DELETE FROM order_details;
DELETE FROM orders;
DELETE FROM product_category;
DELETE FROM products_images;
DELETE FROM product_images;
DELETE FROM products;
DELETE FROM categories;
DELETE FROM password_reset_tokens;
DELETE FROM email_verification_tokens;
DELETE FROM user_audit;
DELETE FROM role_permissions;
DELETE FROM permissions;
DELETE FROM addresses;
DELETE FROM users;
DELETE FROM roles;
ALTER TABLE wishlist_items AUTO_INCREMENT = 1;
ALTER TABLE wishlists AUTO_INCREMENT = 1;
ALTER TABLE reviews AUTO_INCREMENT = 1;
ALTER TABLE order_status_history AUTO_INCREMENT = 1;
ALTER TABLE shippings AUTO_INCREMENT = 1;
ALTER TABLE payments AUTO_INCREMENT = 1;
ALTER TABLE cart_items AUTO_INCREMENT = 1;
ALTER TABLE carts AUTO_INCREMENT = 1;
ALTER TABLE order_coupons AUTO_INCREMENT = 1;
ALTER TABLE coupons AUTO_INCREMENT = 1;
ALTER TABLE order_details AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE product_category AUTO_INCREMENT = 1;
ALTER TABLE products_images AUTO_INCREMENT = 1;
ALTER TABLE product_images AUTO_INCREMENT = 1;
ALTER TABLE products AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;
ALTER TABLE password_reset_tokens AUTO_INCREMENT = 1;
ALTER TABLE email_verification_tokens AUTO_INCREMENT = 1;
ALTER TABLE user_audit AUTO_INCREMENT = 1;
ALTER TABLE role_permissions AUTO_INCREMENT = 1;
ALTER TABLE permissions AUTO_INCREMENT = 1;
ALTER TABLE addresses AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE roles AUTO_INCREMENT = 1;
INSERT INTO roles (role_id, name, description) VALUES
(1, 'Store Owner / Administrator', 'The highest authority in the store, responsible for overall operations and strategic decisions.'),
(2, 'Store Manager', 'Oversees daily operations, staff management, and ensures the store meets its targets.'),
(3, 'Assistant Manager', 'Supports the store manager and may take charge in their absence.'),
(4, 'Sales Associate / Cashier', 'Frontline staff who interact with customers, process sales, and handle transactions.'),
(5, 'Inventory Specialist / Stock Clerk', 'Manages stock levels, receives shipments, and organizes inventory.'),
(6, 'Customer Service Representative', 'Handles customer inquiries, support, and after-sales service.'),
(7, 'Visual Merchandiser', 'Designs and sets up in-store displays to enhance the shopping experience.'),
(8, 'Marketing Manager', 'Develops marketing strategies to drive sales and increase customer engagement.'),
(9, 'Accounts / Finance Officer', 'Manages financial transactions, budgeting, and financial reporting.'),
(10, 'Human Resources Manager', 'Oversees recruitment, employee relations, and compliance with employment laws.'),
(11, 'IT Administrator', 'Maintains the stores technological infrastructure and ensures system security.'),
(12, 'Security Personnel', 'Responsible for the safety and security of the store premises and assets.'),
(13, 'Maintenance Staff', 'Handles the upkeep and repairs of store facilities and equipment.'),
(14, 'Auditor', 'Conducts internal or external audits to ensure compliance and efficiency.'),
(15, 'Compliance Officer', 'Ensures the store adheres to legal standards and internal policies.'),
(16, 'Trainer / Training Coordinator', 'Manages employee training programs and professional development.'),
(17, 'Vendor / Supplier', 'External partners who supply products or services to the store.'),
(18, 'Franchise Coordinator', 'Manages communication and compliance between franchisors and franchisees.'),
(19, 'Customer', 'End-users who interact with the stores digital systems.'),
(20, 'Guest User', 'Users with minimal access, possibly for temporary or restricted purposes.');