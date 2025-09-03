package com.kir.apigateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kir.apigateway.dto.request.ValidateTokenRequest;
import com.kir.apigateway.dto.response.ValidateTokenResponse;
import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final List<String> PUBLIC_ENDPOINTS = List.of(
            "/auth/public/login",
            "/auth/public/token/validate",
            "/auth/account/public/register"
    );

    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (request.getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }

            String uri = request.getURI().getPath();

            log.info("Processing request for URI: {}", uri);

            // Skip authentication for public endpoints
            if (PUBLIC_ENDPOINTS.stream().anyMatch(ep -> ep.equals(uri))) {
                log.info("Skipping authentication for public endpoint: {}", uri);
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header for URI: {}", uri);
                return onError(exchange, ErrorCode.GATEWAY_UNAUTHORIZED);
            }
            String authToken = authHeader.replace("Bearer ", "");
            return validateTokenWithAuthService(authToken)
                    .flatMap(authResponse -> {
                        log.info("Response from auth service");
                        log.info(authResponse.toString());
                        if (authResponse.isValidate()) {
                            log.info("Token validation successful for URI: {}", uri);
                            return chain.filter(exchange);
                        } else {
                            log.warn("Token validation failed for URI: {}", uri);
                            return onError(exchange, ErrorCode.GATEWAY_UNAUTHENTICATED);
                        }
                    })
                    .onErrorResume(error -> {
                        log.error("Error during token validation for URI: {}", uri, error);
                        if (error instanceof WebClientResponseException) {
                            WebClientResponseException wcre = (WebClientResponseException) error;
                            if (wcre.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                                return onError(exchange, ErrorCode.GATEWAY_UNAUTHENTICATED);
                            } else if (wcre.getStatusCode().is5xxServerError()) {
                                return onError(exchange, ErrorCode.GATEWAY_SERVICE_UNREGISTERED);
                            }
                        }
                        return onError(exchange, ErrorCode.GATEWAY_UNAUTHENTICATED);
                    });
        };
    }

    private Mono<ValidateTokenResponse> validateTokenWithAuthService(String authToken) {
        log.info("Processing request for auth service: {}", authToken);
        ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest(authToken);
        return webClientBuilder.build()
                .post()
                .uri("lb://auth-service/auth/public/token/validate")
                .bodyValue(validateTokenRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ValidateTokenResponse>>() {})
                .map(ApiResponse::getData);
    }

    private Mono<Void> onError(ServerWebExchange exchange, ErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String data = null;

        try {
            data = objectMapper.writeValueAsString(ApiResponse.error(errorCode));
        } catch (JsonProcessingException e) {
            log.error("Error serializing error response", e);
            data = "{\"error\":\"Internal server error\"}";
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        return response.writeWith(Mono.just(response.bufferFactory().wrap(data.getBytes())));
    }

    public static class Config {
        // I'm thinking :)
    }
}