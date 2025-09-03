package com.kir.commonservice.constant;

public final class SecurityConstants {

    private SecurityConstants() {}

    //Role
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    // JWT Constants
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_SECRET_KEY = "trainbooking-jwt-secret-key";
    public static final long JWT_EXPIRATION_TIME = 86400000; // 24 hours

    // Security Headers
    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String CUSTOMER_ID_HEADER = "X-Customer-Id";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String INTERNAL_HEADER = "X-Internal-Header";

    //Internal Key
    public static final String INTERNAL_KEY = "fG6bWWeaaZqenQlZvnCRkEI7HPfKQm6DdCl9G0Sr0LfRt5tN3u1GWjGl2iJYNElE1ogqmCwpfLrkzCJ5SQOB3mmrjWYVp7RWB6zhT6lJKjs3O7F5W29rzsjhyTGDhZVO";

    // Permissions (reusing from previous artifact)
    public static final class Permissions {
        // User Management
        public static final String CREATE_USER = "CREATE_USER";
        public static final String READ_USER = "READ_USER";
        public static final String UPDATE_USER = "UPDATE_USER";
        public static final String DELETE_USER = "DELETE_USER";
        public static final String MANAGE_USER_ROLES = "MANAGE_USER_ROLES";
        public static final String VIEW_ALL_USERS = "VIEW_ALL_USERS";

        // Customer Management
        public static final String CREATE_CUSTOMER = "CREATE_CUSTOMER";
        public static final String READ_CUSTOMER = "READ_CUSTOMER";
        public static final String UPDATE_CUSTOMER = "UPDATE_CUSTOMER";
        public static final String DELETE_CUSTOMER = "DELETE_CUSTOMER";
        public static final String VIEW_ALL_CUSTOMERS = "VIEW_ALL_CUSTOMERS";

        // Booking Management
        public static final String CREATE_BOOKING = "CREATE_BOOKING";
        public static final String READ_BOOKING = "READ_BOOKING";
        public static final String UPDATE_BOOKING = "UPDATE_BOOKING";
        public static final String CANCEL_BOOKING = "CANCEL_BOOKING";
        public static final String DELETE_BOOKING = "DELETE_BOOKING";
        public static final String VIEW_ALL_BOOKINGS = "VIEW_ALL_BOOKINGS";
        public static final String MANAGE_BOOKING_STATUS = "MANAGE_BOOKING_STATUS";

        // Order Management
        public static final String CREATE_ORDER = "CREATE_ORDER";
        public static final String READ_ORDER = "READ_ORDER";
        public static final String UPDATE_ORDER = "UPDATE_ORDER";
        public static final String DELETE_ORDER = "DELETE_ORDER";
        public static final String VIEW_ALL_ORDERS = "VIEW_ALL_ORDERS";
        public static final String MANAGE_ORDER_STATUS = "MANAGE_ORDER_STATUS";

        // Payment Management
        public static final String PROCESS_PAYMENT = "PROCESS_PAYMENT";
        public static final String READ_PAYMENT = "READ_PAYMENT";
        public static final String REFUND_PAYMENT = "REFUND_PAYMENT";
        public static final String VIEW_ALL_PAYMENTS = "VIEW_ALL_PAYMENTS";
        public static final String MANAGE_PAYMENT_STATUS = "MANAGE_PAYMENT_STATUS";

        // Ticket Management
        public static final String ISSUE_TICKET = "ISSUE_TICKET";
        public static final String READ_TICKET = "READ_TICKET";
        public static final String UPDATE_TICKET = "UPDATE_TICKET";
        public static final String CANCEL_TICKET = "CANCEL_TICKET";
        public static final String CHECK_IN_PASSENGER = "CHECK_IN_PASSENGER";
        public static final String VIEW_ALL_TICKETS = "VIEW_ALL_TICKETS";

        // Trip & Search
        public static final String SEARCH_TRIPS = "SEARCH_TRIPS";
        public static final String READ_TRIP = "READ_TRIP";
        public static final String CREATE_TRIP = "CREATE_TRIP";
        public static final String UPDATE_TRIP = "UPDATE_TRIP";
        public static final String DELETE_TRIP = "DELETE_TRIP";
        public static final String MANAGE_TRIP_STATUS = "MANAGE_TRIP_STATUS";

        // Seat Selection
        public static final String SELECT_SEAT = "SELECT_SEAT";
        public static final String VIEW_SEAT_MAP = "VIEW_SEAT_MAP";
        public static final String MANAGE_SEAT_AVAILABILITY = "MANAGE_SEAT_AVAILABILITY";
        public static final String RESERVE_SEAT = "RESERVE_SEAT";

        // Notifications
        public static final String SEND_NOTIFICATION = "SEND_NOTIFICATION";
        public static final String READ_NOTIFICATION = "READ_NOTIFICATION";
        public static final String MANAGE_NOTIFICATION_TEMPLATES = "MANAGE_NOTIFICATION_TEMPLATES";
        public static final String VIEW_ALL_NOTIFICATIONS = "VIEW_ALL_NOTIFICATIONS";

        // Events & Promotions
        public static final String CREATE_EVENT = "CREATE_EVENT";
        public static final String READ_EVENT = "READ_EVENT";
        public static final String UPDATE_EVENT = "UPDATE_EVENT";
        public static final String DELETE_EVENT = "DELETE_EVENT";
        public static final String MANAGE_PROMOTIONS = "MANAGE_PROMOTIONS";
        public static final String CREATE_VOUCHER = "CREATE_VOUCHER";
        public static final String MANAGE_VOUCHER_USAGE = "MANAGE_VOUCHER_USAGE";

        // System Administration
        public static final String MANAGE_SYSTEM_CONFIG = "MANAGE_SYSTEM_CONFIG";
        public static final String VIEW_SYSTEM_LOGS = "VIEW_SYSTEM_LOGS";
        public static final String BACKUP_RESTORE = "BACKUP_RESTORE";
        public static final String MANAGE_PERMISSIONS = "MANAGE_PERMISSIONS";
    }

    // Roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    // Common Security Expressions
    public static final class Expressions {
        public static final String ADMIN_ONLY = "hasAuthority('" + ROLE_ADMIN + "')";
        public static final String USER_OR_ADMIN = "hasAnyAuthority('" + ROLE_USER + "', '" + ROLE_ADMIN + "')";
        public static final String OWN_RESOURCE_OR_ADMIN =
                "hasAuthority('" + ROLE_ADMIN + "') or #userId == authentication.principal.id";
        public static final String OWN_CUSTOMER_OR_ADMIN =
                "hasAuthority('" + ROLE_ADMIN + "') or #customerId == authentication.principal.customerId";
    }
}
