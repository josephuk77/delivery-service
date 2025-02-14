package com.sparta.delivery.aaglobal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient(){
    return WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent")
        .defaultHeader("Content-Type", "application/json") // 기본 헤더 (선택)
        .build();
  }
}
