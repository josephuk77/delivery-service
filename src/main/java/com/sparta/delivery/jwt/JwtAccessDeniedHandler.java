package com.sparta.delivery.jwt;

import com.sparta.delivery.aaglobal.GlobalException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

// 인가 예외 처리
@Component
@Slf4j(topic = "JwtAccessDeniedHandler")
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    Throwable cause = accessDeniedException.getCause();
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setCharacterEncoding("UTF-8");

    // AccessDeniedException의 원인이 GlobalException인 경우
    if (cause instanceof GlobalException) {
      GlobalException globalEx = (GlobalException) cause;
      log.error("Global Exception: {}, message: {}",
          globalEx.getStatus(), globalEx.getMessage());
      response.getWriter().write(globalEx.getMessage());
      response.getWriter().flush();
    } else {  // 기본 인가 실패 처리
      log.error("Forbidden error: {}", accessDeniedException.getMessage());
      response.getWriter().write(accessDeniedException.getMessage());
      response.getWriter().flush();
    }
  }
}
