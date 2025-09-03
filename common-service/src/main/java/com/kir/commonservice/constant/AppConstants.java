package com.kir.commonservice.constant;

public final class AppConstants {

    private AppConstants() {}

    // Service Names
    public static final String AUTH_SERVICE = "auth-service"; //
    public static final String BOOKING_SERVICE = "booking-service"; //
    public static final String PAYMENT_SERVICE = "payment-service"; //
    public static final String NOTIFICATION_SERVICE = "notification-service"; //
    public static final String SEARCH_SERVICE = "search-service"; //
    public static final String TICKET_SERVICE = "ticket-service"; //
    public static final String CUSTOMER_SERVICE = "passenger-service"; //
    public static final String EVENT_SERVICE = "event-service"; //
    public static final String SEAT_SERVICE = "seat-selection-service"; //

    // Status Constants
    public static final class BookingStatus {
        public static final String CREATED = "CREATED";
        public static final String CONFIRMED = "CONFIRMED";
        public static final String CANCELLED = "CANCELLED";
    }

    public static final class PaymentStatus {
        public static final String PENDING = "PENDING";
        public static final String PAID = "PAID";
        public static final String FAILED = "FAILED";
        public static final String REFUNDED = "REFUNDED";
    }

    public static final class NotificationStatus {
        public static final String PENDING = "PENDING";
        public static final String SENT = "SENT";
        public static final String FAILED = "FAILED";
        public static final String READ = "READ";
    }

    //Passenger Service
    public enum DocumentType{
        CCCD("CAN CUOC CONG DAN"),
        HC("HO CHIEU")
        ;
        private final String label;
        DocumentType(String label){
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }

    //Search Service
    public enum TripType {
        BUS, FLIGHT, TRAIN;
    }

    public enum LocationType {
        AIRPORT, TRAIN_STATION, BUS_STATION;
    }

    public enum TripStatus {
        SCHEDULED, DELAYED, CANCELLED, COMPLETED;
    }

    // Business Constants
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int SEAT_RESERVATION_TIMEOUT_MINUTES = 15;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int OTP_LENGTH = 6;
    public static final int OTP_EXPIRY_MINUTES = 5;
}
