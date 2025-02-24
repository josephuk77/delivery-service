package com.sparta.delivery.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.delivery.user.dto.UserRequestDto;
import com.sparta.delivery.user.entity.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtil jwtUtil;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final RedisTemplate<String, String> redisTemplate;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtAuthenticationEntryPoint entryPoint, RedisTemplate<String, String> redisTemplate) {
    this.jwtUtil = jwtUtil;
    this.jwtAuthenticationEntryPoint = entryPoint;
    this.redisTemplate = redisTemplate;
    setFilterProcessesUrl("/users/login");
  }

  @Override
  public Authentication attemptAuthentication
      (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      UserRequestDto requestDto = new ObjectMapper()
          .readValue(request.getInputStream(), UserRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getUsername(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
    UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getRole();

    String token = jwtUtil.createAccessToken(username, role);
    jwtUtil.addJwtToHeader(token, response);

    String refreshToken = jwtUtil.createRefreshToken();
    redisTemplate.opsForValue().set(username, refreshToken);
  }

  @Override
  protected void unsuccessfulAuthentication
      (HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    jwtAuthenticationEntryPoint.commence(request, response, failed);
  }
}
