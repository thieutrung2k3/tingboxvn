package com.kir.passengerservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final Logger requestLogger = LoggerFactory.getLogger("REQUEST_LOGGER");
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        request.setAttribute("startTime", System.currentTimeMillis());

        response.setHeader(REQUEST_ID_HEADER, requestId);

        logRequest(request, requestId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String requestId = (String) request.getAttribute("requestId");

        logResponse(response, request, requestId, duration, ex);
    }

    private void logResponse(HttpServletResponse response, HttpServletRequest request, String requestId, long duration, Exception ex) {
        try{
            Map<String, Object> logData = new HashMap<>();
            logData.put("type", "RESPONSE");
            logData.put("requestId", requestId);
            logData.put("method", request.getMethod());
            logData.put("uri", request.getRequestURI());
            logData.put("status", response.getStatus());
            logData.put("duration", duration);

            if(ex != null){
                logData.put("exception", ex.getClass().getSimpleName());
                logData.put("exceptionMsg", ex.getMessage());
            }

            requestLogger.info(objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            requestLogger.error("Failed to log response: {}", e.getMessage());
        }
    }

    private void logRequest(HttpServletRequest request, String requestId) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("type", "REQUEST");
            logData.put("requestId", requestId);
            logData.put("method", request.getMethod());
            logData.put("uri", request.getRequestURI());
            logData.put("queryString", request.getQueryString());
            logData.put("remoteAddress", getClientIpAddress(request));
            logData.put("userAgent", request.getHeader("User-Agent"));
            logData.put("contentType", request.getContentType());
            logData.put("contentLength", request.getContentLength());

            Map<String, String> headers = new HashMap<>();
            request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                if (!isSentitiveHeader(headerName)) {
                    headers.put(headerName, request.getHeader(headerName));
                }
            });
            logData.put("headers", headers);

            requestLogger.info(objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            requestLogger.error("Failed to log request: {}", e.getMessage());
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }

    private boolean isSentitiveHeader(String headerName) {
        return headerName.toLowerCase().equalsIgnoreCase("authorization")
                || headerName.toLowerCase().equalsIgnoreCase("cookie")
                || headerName.toLowerCase().equalsIgnoreCase("token");
    }
}
