-- country table
Create Table If Not Exists countries (
  country_id Int Primary Key Auto_Increment,
  name Varchar(255) Not Null,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp,
);

-- state table
Create Table If Not Exists states (
  state_id Int Primary Key Auto_Increment,
  name Varchar(255) Not Null,
  country_id Int Not Null,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp,
  Constraint Foreign Key (country_id) References countries (country_id) On Delete Cascade On Update Cascade
);

-- cities table
Create Table If Not Exists cities (
  city_id Int Primary Key Auto_Increment,
  name Varchar(255) Not Null,
  state_id Int Not Null,
  created_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (state_id) References states (state_id) On Delete Cascade On Update Cascade
);

-- zip code tables
Create Table If Not Exists zip_codes (
  zip_code_id Int Primary Key Auto_Increment,
  zip_code Varchar(50) Not Null,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp
);

-- street table
Create Table If Not Exists streets (
  street_id Int Primary Key Auto_Increment,
  name Varchar(255) Not Null,
  zip_code_id Int Not Null,
  city_id Int Not Null,
  created_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (zip_code_id) References zip_codes (zip_code_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (city_id) References cities (city_id) On Delete Cascade On Update Cascade
);

-- roles table
Create Table If Not Exists roles (
  role_id Int Primary Key Auto_Increment,
  name Varchar(50) Not Null Unique,
  description Varchar(155) Not Null,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp
);

-- users table
Create Table If Not Exists users (
  user_id Int Primary Key Auto_Increment,
  username Varchar(50) Unique Not Null,
  salt Varchar(255) Not Null,
  hash Varchar(255) Not Null, -- bcrypt, Argon2, or PBKDF2
  email Varchar(320) Unique Not Null,
  first_name Varchar(255),
  last_name Varchar(255),
  phone Varchar(15), -- E.164 format
  is_active Boolean Default false,
  user_role_id int Default 20,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp,
  Constraint Foreign Key (user_role_id) References roles (role_id) On Delete Cascade On Update Cascade
);

-- addresses
Create Table If Not Exists customer_addresses (
  address_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  street_address Varchar(255) Not Null,
  address_number Varchar(50) Not Null,
  address_type Varchar(50) Not Null,  -- 'billing', 'shipping', 'home', 'work'
  city Varchar(255) Not Null,
  state Varchar(255),
  zip_code Varchar(50) Not Null,
  country Varchar(255) Not Null,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade
);

-- addresses
Create Table If Not Exists addresses (
  address_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  street_id Int Not Null,
  address_number Varchar(50),
  address_type Varchar(50) Not Null,  -- 'billing', 'shipping', 'home', 'work'
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (street_id) References streets (street_id) On Delete Cascade On Update Cascade
);

-- permissions table
Create Table If Not Exists permissions (
  permission_id Int Primary Key Auto_Increment,
  name Varchar(50) Not Null Unique,
  description Varchar(155),
  created_at Timestamp Default Current_Timestamp
);

-- N to N role_permissions table
Create Table If Not Exists role_permissions (
  role_id Int,
  permission_id Int,
  created_at Timestamp Default Current_Timestamp,
  Primary Key (role_id, permission_id),
  Constraint Foreign Key (role_id) References roles (role_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (permission_id) References permissions (permission_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists user_audit (
  audit_id Int Primary Key Auto_Increment,
  user_id Int,
  action Varchar(50), -- 'CREATE', 'UPDATE', 'DELETE'
  changed_data Text,
  changed_at Timestamp Default Current_Timestamp,
  changed_by Int,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (changed_by) References users (user_id)
);

Create Table If Not Exists email_verification_tokens (
  token_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  token Varchar(255) Not Null,
  created_at Timestamp Default Current_Timestamp,
  expires_at Timestamp,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists password_reset_tokens (
  token_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  token Varchar(255) Not Null,
  created_at Timestamp Default Current_Timestamp,
  expires_at Timestamp,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade
);


Create Table If Not Exists categories (
  category_id Int Primary Key Auto_Increment,
  name Varchar(255) Not Null,
  description Varchar(510),
  is_active Boolean Default false,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp
);

Create Table If Not Exists products (
  product_id Int Primary Key Auto_Increment,
  name Varchar(255) Not Null,
  description Varchar(510),
  price Decimal(10,2) Not Null,
  stock Int Not Null,
  is_active Boolean Default false,
  created_at Timestamp Default Current_Timestamp,
  updated_at Timestamp Default Current_Timestamp On Update Current_Timestamp
);

Create Table If Not Exists product_images (
  product_image_id Int Primary Key Auto_Increment,
  image_url Varchar(255) Not Null,
  is_main Boolean Default false
);

Create Table If Not Exists products_images (
  product_image_id Int,
  product_id Int,
  Primary Key (product_image_id, product_id),
  Constraint Foreign Key (product_image_id) References product_images (product_image_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (product_id) References products (product_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists product_category (
  product_id Int,
  category_id Int,
  Primary Key (product_id, category_id),
  Constraint Foreign Key (product_id) References products (product_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (category_id) References categories (category_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists orders (
  order_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  order_date Timestamp Default Current_Timestamp,
  total_amount Decimal(10,2) Not Null,
  status Varchar(50)  Default 'pending',
  shipping_address_id Int Not Null,
  billing_address_id Int Not Null,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (shipping_address_id) References addresses (address_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (billing_address_id) References addresses (address_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists order_details (
  order_detail_id Int Primary Key Auto_Increment,
  order_id Int Not Null,
  product_id Int Not Null,
  quantity Int Not Null,
  unit_price Decimal(10,2) Not Null,
  Constraint Foreign Key (order_id) References orders (order_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (product_id) References products (product_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists coupons (
  coupon_id Int Primary Key Auto_Increment,
  code Varchar(50) Not Null Unique,
  description Varchar(255),
  discount_type Varchar(50), -- 'percentage', 'amount'
  discount_value Decimal(10,2) Not Null,
  expiration_date Date,
  usage_limit Int,
  created_at Timestamp Default Current_Timestamp
);

Create Table If Not Exists order_coupons (
  order_coupon_id Int Primary Key Auto_Increment,
  order_id Int Not Null,
  coupon_id Int Not Null,
  Constraint Foreign Key (order_id) References orders (order_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (coupon_id) References coupons (coupon_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists carts (
  cart_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  created_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists cart_items (
  cart_item_id Int Primary Key Auto_Increment,
  cart_id Int Not Null,
  product_id Int Not Null,
  quantity Int Not Null,
  added_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (cart_id) References carts (cart_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (product_id) References products (product_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists payments (
  payment_id Int Primary Key Auto_Increment,
  order_id Int Not Null,
  payment_date Timestamp Default Current_Timestamp,
  amount Decimal(10,2) Not Null,
  payment_method Varchar(50) Not Null,
  transaction_id Varchar(255),
  status Varchar(50) Default 'pending',
  Constraint Foreign Key (order_id) References orders (order_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists shippings (
  shipping_id Int Primary Key Auto_Increment,
  order_id Int Not Null,
  shipping_date Timestamp,
  shipping_method Varchar(50) Not Null,
  carrier Varchar(255) Not Null,
  tracking_number Varchar(255),
  status Varchar(50) Default 'pending',
  Constraint Foreign Key (order_id) References orders (order_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists order_status_history (
  order_status_history_id Int Primary Key Auto_Increment,
  order_id Int Not Null,
  status Varchar(50) Default 'pending',
  changed_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (order_id) References orders (order_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists reviews (
  review_id Int Primary Key Auto_Increment,
  product_id Int Not Null,
  user_id Int Not Null,
  rating Int Not Null Check (rating >= 1 And rating <= 5),
  comment Varchar(500),
  approved Boolean Default false,
  created_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (product_id) References products (product_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists wishlists (
  wishlist_id Int Primary Key Auto_Increment,
  user_id Int Not Null,
  created_at Timestamp Default Current_Timestamp,
  Constraint Foreign Key (user_id) References users (user_id) On Delete Cascade On Update Cascade
);

Create Table If Not Exists wishlist_items (
  wishlist_id Int Not Null,
  product_id Int Not Null,
  added_at Timestamp Default Current_Timestamp,
  Primary Key (wishlist_id, product_id),
  Constraint Foreign Key (wishlist_id) References wishlists (wishlist_id) On Delete Cascade On Update Cascade,
  Constraint Foreign Key (product_id) References products (product_id) On Delete Cascade On Update Cascade
);
