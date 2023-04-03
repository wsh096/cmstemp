package com.zerobase.user.config;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationprovider() {
        return new JwtAuthenticationProvider();
    }
}
