package com.kosmin.authorization.config;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class SecurityConfig {

  @Value("${spring.profiles.active}")
  private String springProfile;

  @Value("${authorization.service.jwt.secret}")
  private String jwtSecret;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    String requestMatchers =
        "local".equalsIgnoreCase(springProfile) ? "/**" : "/authorization/v1/health";
    return (web) -> web.ignoring().requestMatchers(requestMatchers);
  }

  @Bean
  public Key jwtKey() {
    return new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
  }
}
