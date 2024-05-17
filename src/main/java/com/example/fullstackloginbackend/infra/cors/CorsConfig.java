package com.example.fullstackloginbackend.infra.cors;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    Dotenv dotenv = Dotenv.configure().load();

    registry.addMapping("/**")
        .allowedOrigins(dotenv.get("FRONTEND_URL"))
        .allowedMethods("GET", "POST", "PUT", "DELETE");
  }
}
