package org.example.test1.common.config;

import org.example.test1.common.interceptor.JwtLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtLoginInterceptor jwtLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/employee/login",
                        "/employee/logout",
                        "/user/login",
                        "/common/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html",
                        "/webjars/**"
                );
    }
}
