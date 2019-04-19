package com.yzj.wxpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication
public class WxpayApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WxpayApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedMethods("POST","GET","DELETE","PUT","OPTIONS")
                .maxAge(3600);
    }
}
