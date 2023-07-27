package com.example.sv.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Điều này sẽ áp dụng CORS cho tất cả các endpoint trong /api/**
                .allowedOrigins("http://localhost:3000") // Cho phép yêu cầu từ trang web chạy tại localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các phương thức yêu cầu
                .allowedHeaders("*") // Cho phép tất cả các tiêu đề trong yêu cầu
                .allowCredentials(true) // Cho phép gửi cookie trong yêu cầu (nếu áp dụng)
                .maxAge(3600); // Thời gian lưu trữ kết quả Preflight trong trình duyệt, tính bằng giây
    }
}
