package com.sparta.delivery.aconfig;

import com.sparta.delivery.aaglobal.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaConfig {

  @Bean
  public AuditorAware<Long> auditorProvider() {
    return new AuditorAwareImpl();
  }
}
