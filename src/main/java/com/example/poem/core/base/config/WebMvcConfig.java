package com.example.poem.core.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //TODO dowiedziec sie czy ta śceiżka tak moze byc
    registry.addResourceHandler("/images/**")
        .addResourceLocations("file:C:\\Programowanie\\Projekty\\poem\\poem\\images/");
    registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/");

  }
}
