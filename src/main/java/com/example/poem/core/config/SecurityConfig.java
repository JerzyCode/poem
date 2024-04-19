package com.example.poem.core.config;

import com.example.poem.core.base.authorization.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter filter;
  private static final String[] ALLOWED_URLS = {
      "/h2-console/**",
      "/authentication/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.headers(headers ->
        headers.xssProtection(
            xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
        ).contentSecurityPolicy(
            cps -> cps.policyDirectives("script-src 'self'")
        ));
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/**").permitAll()
            .anyRequest().authenticated())
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    return http.build();
  }

  @Bean
  @Profile("mock")
  public SecurityFilterChain devFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(ALLOWED_URLS).permitAll()
            .anyRequest().authenticated())
        .authenticationManager(authenticationManager)
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .build();
  }
}
