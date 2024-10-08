package com.example.poem.core.base.config;

import com.example.poem.core.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private static final String HOME = "/home";

  private static final String[] ALLOWED_URLS = {
      "/assets/**",
      "/h2-console/**",
      HOME,
      "/signup",
      "/verses/**",
      "/verse/**",
      "/login/**",
      "/css/**",
      "/images/**",
      "/js/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(Customizer.withDefaults())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(ALLOWED_URLS).permitAll()
            .anyRequest().authenticated())
        .formLogin(formLogin -> formLogin.loginPage("/login").permitAll()
            .defaultSuccessUrl(HOME, true)
            .failureUrl("/login?error"))
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl(HOME)
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .permitAll())
        .headers(headers ->
            headers.xssProtection(
                xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
            ).contentSecurityPolicy(
                cps -> cps.policyDirectives("script-src 'self'")
            ))
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(authProvider());
    return authenticationManagerBuilder.build();
  }

}
