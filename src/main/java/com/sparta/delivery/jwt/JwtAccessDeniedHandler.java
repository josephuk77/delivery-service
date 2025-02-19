package com.sparta.delivery.jwt;

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

    String errorMessage = accessDeniedException.getMessage();
    log.error("Forbidden error: {}", errorMessage);

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(errorMessage);
  }
}
