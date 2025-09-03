package org.kir.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/health", "favicon.ico");
    }

    // CORS được xử lý bởi API Gateway, không cần config ở đây
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**")
    // .allowedOriginPatterns("*")
    // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
    // .allowedHeaders("*")
    // .allowCredentials(true)
    // .maxAge(3600);
    // }
}
