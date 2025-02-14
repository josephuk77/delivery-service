package com.sparta.delivery.aaglobal;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<Long> {

  @Override
  public Optional<Long> getCurrentAuditor() {
    Long userId = 1L; // 로그인 기능 구현 후 수정 예정
    return Optional.of(userId);
  }
}
