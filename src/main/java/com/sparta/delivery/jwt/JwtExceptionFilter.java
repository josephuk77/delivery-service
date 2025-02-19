package com.sparta.delivery.jwt;

import com.sparta.delivery.aaglobal.GlobalException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Authorization 필터에서 발생한 GlobalException을 처리하는 필터
@Component
@Slf4j(topic = "JwtExceptionFilter")
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (GlobalException e) {
      log.error("ERROR: {}, URL: {}, MESSAGE: {}", e.getStatus(),
          request.getRequestURI(), e.getMessage());

      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      response.getWriter().write(e.getMessage());
    }
  }
}
