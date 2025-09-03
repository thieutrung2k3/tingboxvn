package com.kir.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class CorsConfig {

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();

                // Chỉ set origin một lần
                if (!headers.containsKey("Access-Control-Allow-Origin")) {
                    headers.add("Access-Control-Allow-Origin", "*");
                }
                if (!headers.containsKey("Access-Control-Allow-Methods")) {
                    headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                }
                if (!headers.containsKey("Access-Control-Max-Age")) {
                    headers.add("Access-Control-Max-Age", "3600");
                }
                if (!headers.containsKey("Access-Control-Allow-Headers")) {
                    headers.add("Access-Control-Allow-Headers", "*");
                }

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx).doOnNext(aVoid -> {
                // Ensure CORS headers are present in main response
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                if (CorsUtils.isCorsRequest(request)) {
                    if (!headers.containsKey("Access-Control-Allow-Origin")) {
                        headers.add("Access-Control-Allow-Origin", "*");
                    }
                }
            });
        };
    }
}