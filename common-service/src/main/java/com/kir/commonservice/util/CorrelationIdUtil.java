package com.kir.commonservice.util;

import java.util.UUID;

public final class CorrelationIdUtil {

    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

    private CorrelationIdUtil() {}

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    public static void set(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    public static String get() {
        return CORRELATION_ID.get();
    }

    public static void clear() {
        CORRELATION_ID.remove();
    }
}
