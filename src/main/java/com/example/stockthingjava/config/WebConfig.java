package com.example.stockthingjava.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow frontend to make requests to backend
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Your frontend URL (React or other)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials if needed (e.g., cookies or authentication headers)
    }
}

