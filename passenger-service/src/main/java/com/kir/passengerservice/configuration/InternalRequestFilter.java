package com.kir.passengerservice.configuration;

import com.kir.commonservice.constant.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class InternalRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String internalKey = request.getHeader(SecurityConstants.INTERNAL_HEADER);
        String uri = request.getRequestURI();
        log.debug("URI: ");
        log.debug(uri);
        if (uri.startsWith("/internal") || uri.contains("/internal/")) {
            if(!internalKey.equals(SecurityConstants.INTERNAL_KEY)) {
                response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
