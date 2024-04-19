package com.example.poem.core.base.authorization;

import com.example.poem.core.models.user.User;
import com.example.poem.core.models.user.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static com.example.poem.core.base.Constants.TEST_PASSWORD;
import static com.example.poem.core.base.Constants.TEST_USERNAME;
import static com.example.poem.core.base.authorization.JwtAuthenticationFilter.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
  @Mock
  JwtService jwtService;
  @Mock
  UserDetailsService userDetailsService;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  FilterChain filterChain;
  @InjectMocks
  JwtAuthenticationFilter sut;

  @AfterEach
  void clearSecurityContextHolder() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void do_filter_valid_authorization_header() throws ServletException, IOException {
    //given
    String validToken = "Bearer valid Token";
    String extractedToken = "valid Token";
    when(request.getHeader(AUTHORIZATION)).thenReturn(validToken);
    when(jwtService.extractUsername(extractedToken)).thenReturn(TEST_USERNAME);
    UserDetails user = User.builder()
        .username(TEST_USERNAME)
        .password(TEST_PASSWORD)
        .role(UserRole.USER)
        .build();
    when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(user);
    when(jwtService.isTokenValid(extractedToken, user)).thenReturn(true);

    //when
    sut.doFilterInternal(request, response, filterChain);

    //then
    verify(filterChain).doFilter(request, response);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
  }

  @Test
  void do_filter_invalid_no_bearer_authorization_header() throws ServletException, IOException {
    //given
    String validToken = "invalid Token";

    //when
    when(request.getHeader("Authorization")).thenReturn(validToken);
    sut.doFilterInternal(request, response, filterChain);

    //then
    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void do_filter_no_valid_token_authorization_header() throws ServletException, IOException {
    //given
    String invalidToken = "Bearer invalid Token";
    String extractedToken = "invalid Token";
    UserDetails user = User.builder()
        .username(TEST_USERNAME)
        .password(TEST_PASSWORD)
        .role(UserRole.USER)
        .build();
    when(request.getHeader("Authorization")).thenReturn(invalidToken);
    when(jwtService.extractUsername(extractedToken)).thenReturn(TEST_USERNAME);
    when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(user);
    when(jwtService.isTokenValid(extractedToken, user)).thenReturn(false);

    //when
    sut.doFilterInternal(request, response, filterChain);

    //then
    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());

  }

  @Test
  void do_filter_no_authorization_header() throws ServletException, IOException {
    //given
    when(request.getHeader(AUTHORIZATION)).thenReturn(null);

    //when
    sut.doFilterInternal(request, response, filterChain);

    //then
    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

}