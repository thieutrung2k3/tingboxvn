-- ========================================
-- INSERT ROLES
-- ========================================
INSERT INTO roles (name, description)
VALUES ('ROLE_ADMIN', 'Administrator with full system access'),
       ('ROLE_USER', 'Regular user with basic booking functionality');

-- ========================================
-- INSERT PERMISSIONS
-- ========================================

-- User Management Permissions
INSERT INTO permissions (name, description)
VALUES ('CREATE_USER', 'Create new user accounts'),
       ('READ_USER', 'View user information'),
       ('UPDATE_USER', 'Update user information'),
       ('DELETE_USER', 'Delete user accounts'),
       ('MANAGE_USER_ROLES', 'Assign/remove roles from users'),
       ('VIEW_ALL_USERS', 'View all users in the system'),

-- Customer Management Permissions
       ('CREATE_CUSTOMER', 'Create customer profiles'),
       ('READ_CUSTOMER', 'View customer information'),
       ('UPDATE_CUSTOMER', 'Update customer information'),
       ('DELETE_CUSTOMER', 'Delete customer profiles'),
       ('VIEW_ALL_CUSTOMERS', 'View all customers in the system'),

-- Booking Management Permissions
       ('CREATE_BOOKING', 'Create new bookings'),
       ('READ_BOOKING', 'View booking information'),
       ('UPDATE_BOOKING', 'Update booking details'),
       ('CANCEL_BOOKING', 'Cancel bookings'),
       ('DELETE_BOOKING', 'Delete booking records'),
       ('VIEW_ALL_BOOKINGS', 'View all bookings in the system'),
       ('MANAGE_BOOKING_STATUS', 'Change booking status'),

-- Order Management Permissions
       ('CREATE_ORDER', 'Create new orders'),
       ('READ_ORDER', 'View order information'),
       ('UPDATE_ORDER', 'Update order details'),
       ('DELETE_ORDER', 'Delete order records'),
       ('VIEW_ALL_ORDERS', 'View all orders in the system'),
       ('MANAGE_ORDER_STATUS', 'Change order status'),

-- Payment Management Permissions
       ('PROCESS_PAYMENT', 'Process payment transactions'),
       ('READ_PAYMENT', 'View payment information'),
       ('REFUND_PAYMENT', 'Process payment refunds'),
       ('VIEW_ALL_PAYMENTS', 'View all payment records'),
       ('MANAGE_PAYMENT_STATUS', 'Change payment status'),

-- Ticket Management Permissions
       ('ISSUE_TICKET', 'Issue tickets for confirmed bookings'),
       ('READ_TICKET', 'View ticket information'),
       ('UPDATE_TICKET', 'Update ticket details'),
       ('CANCEL_TICKET', 'Cancel issued tickets'),
       ('CHECK_IN_PASSENGER', 'Check-in passengers'),
       ('VIEW_ALL_TICKETS', 'View all tickets in the system'),

-- Search & Trip Management Permissions
       ('SEARCH_TRIPS', 'Search for available trips'),
       ('READ_TRIP', 'View trip information'),
       ('CREATE_TRIP', 'Create new trips'),
       ('UPDATE_TRIP', 'Update trip information'),
       ('DELETE_TRIP', 'Delete trip records'),
       ('MANAGE_TRIP_STATUS', 'Change trip status'),

-- Seat Selection Permissions
       ('SELECT_SEAT', 'Select seats for bookings'),
       ('VIEW_SEAT_MAP', 'View seat availability maps'),
       ('MANAGE_SEAT_AVAILABILITY', 'Manage seat availability'),
       ('RESERVE_SEAT', 'Reserve seats temporarily'),

-- Notification Management Permissions
       ('SEND_NOTIFICATION', 'Send notifications to customers'),
       ('READ_NOTIFICATION', 'View notification records'),
       ('MANAGE_NOTIFICATION_TEMPLATES', 'Manage email/SMS templates'),
       ('VIEW_ALL_NOTIFICATIONS', 'View all notification records'),

-- Event & Promotion Management Permissions
       ('CREATE_EVENT', 'Create promotional events'),
       ('READ_EVENT', 'View event information'),
       ('UPDATE_EVENT', 'Update event details'),
       ('DELETE_EVENT', 'Delete events'),
       ('MANAGE_PROMOTIONS', 'Manage promotions and vouchers'),
       ('CREATE_VOUCHER', 'Create voucher codes'),
       ('MANAGE_VOUCHER_USAGE', 'Track and manage voucher usage'),

-- Provider Management Permissions
       ('CREATE_PROVIDER', 'Add new service providers'),
       ('READ_PROVIDER', 'View provider information'),
       ('UPDATE_PROVIDER', 'Update provider details'),
       ('DELETE_PROVIDER', 'Remove providers'),
       ('MANAGE_PROVIDER_PAYMENTS', 'Manage payments to providers'),

-- Reporting & Analytics Permissions
       ('VIEW_REPORTS', 'View system reports'),
       ('EXPORT_DATA', 'Export system data'),
       ('VIEW_ANALYTICS', 'View system analytics'),
       ('GENERATE_REPORTS', 'Generate custom reports'),

-- System Administration Permissions
       ('MANAGE_SYSTEM_CONFIG', 'Manage system configuration'),
       ('VIEW_SYSTEM_LOGS', 'View system activity logs'),
       ('BACKUP_RESTORE', 'Perform backup and restore operations'),
       ('MANAGE_PERMISSIONS', 'Manage user permissions');

-- ========================================
-- ASSIGN PERMISSIONS TO ADMIN ROLE
-- ========================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
       id
FROM permissions;

-- ========================================
-- ASSIGN PERMISSIONS TO USER ROLE
-- ========================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id as role_id,
       p.id as permission_id
FROM roles r,
     permissions p
WHERE r.name = 'ROLE_USER'
  AND p.name IN (
    -- Customer self-management
                 'READ_CUSTOMER',
                 'UPDATE_CUSTOMER',

    -- Booking operations
                 'CREATE_BOOKING',
                 'READ_BOOKING',
                 'UPDATE_BOOKING',
                 'CANCEL_BOOKING',

    -- Order operations (own orders only)
                 'CREATE_ORDER',
                 'READ_ORDER',

    -- Payment operations (own payments only)
                 'PROCESS_PAYMENT',
                 'READ_PAYMENT',

    -- Ticket operations (own tickets only)
                 'READ_TICKET',
                 'CHECK_IN_PASSENGER',

    -- Search functionality
                 'SEARCH_TRIPS',
                 'READ_TRIP',

    -- Seat selection
                 'SELECT_SEAT',
                 'VIEW_SEAT_MAP',
                 'RESERVE_SEAT',

    -- Notifications (own notifications only)
                 'READ_NOTIFICATION',

    -- Events and promotions (view only)
                 'READ_EVENT'
    );
