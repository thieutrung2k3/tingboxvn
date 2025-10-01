package org.kir.searchservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir.commonservice.constant.SecurityConstants;
import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomJwtDecoder customJwtDecoder;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeHttpRequests(request ->
//                        request
//                                .requestMatchers("/ws/**", "/*/ws/**").permitAll()
//                                .requestMatchers("/*/public/**").permitAll()
//                                .requestMatchers("/public/**").permitAll()
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
//                                .requestMatchers("/*/admin/**").hasAuthority(SecurityConstants.ADMIN)
//                                .requestMatchers("/*/user/**").hasAuthority(SecurityConstants.USER)
//                                .requestMatchers("/internal/**", "/*/internal/**").permitAll()
//                        .anyRequest()
//                        .authenticated());
//        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
//                        .decoder(customJwtDecoder)
//                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                .authenticationEntryPoint((request, response, authException) -> {
//                    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
//                    response.setStatus(errorCode.getHttpStatus().value());
//                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//                    ApiResponse<?> apiResponse = new ApiResponse<>();
//                    apiResponse.setCode(errorCode.getCode());
//                    apiResponse.setMessage(errorCode.getMessage());
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    response.getWriter().write(ErrorCode.UNAUTHENTICATED.getMessage());
//                    response.flushBuffer();
//                }));
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//        return httpSecurity.build();
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
