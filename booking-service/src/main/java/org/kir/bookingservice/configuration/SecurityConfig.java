package org.kir.bookingservice.configuration;

import com.kir.commonservice.constant.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/*/public/**").permitAll()
                                .requestMatchers("/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                                .requestMatchers("/*/admin/**").hasAuthority(SecurityConstants.ADMIN)
                                .requestMatchers("/*/user/**").hasAuthority(SecurityConstants.USER)
                                .requestMatchers("/internal/**", "/*/internal/**").permitAll()
                        .anyRequest()
                        .authenticated());

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
