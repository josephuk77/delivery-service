package com.sparta.delivery.jwt;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j(topic = "JwtAuthorizationFilter")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;
  private final RedisTemplate<String, String> redisTemplate;

  @Override
  protected void doFilterInternal
          (HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
          throws ServletException, IOException {

    String requestURI = req.getRequestURI();
    if (requestURI.equals("/users/signup")) {
      filterChain.doFilter(req, res);
      return;
    }

    String accessToken = jwtUtil.getTokenFromRequest(req);
    if(StringUtils.hasText(accessToken)) {
      accessToken = jwtUtil.substringToken(accessToken);

      // access token 에서 claim 가져오기
      Claims accessClaims = getAccessClaims(accessToken);

      // access token claim 으로 refresh 토큰 가져오기
      String refreshToken = redisTemplate.opsForValue().get(accessClaims.getSubject());

      // refresh token 이 완전하면
      if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {

        // refresh token에서 sub, role 추출
        Claims refreshClaims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = refreshClaims.getSubject();
        UserRoleEnum userRoleEnum = jwtUtil.getUserRoleFromClaims(refreshClaims);

        // access token이 만료되었다면
        if (!jwtUtil.validateToken(accessToken)) {

          // 새로운 access token을 발급하여 헤더에 넣어줌
          String newAccessToken = jwtUtil.createAccessToken(username, userRoleEnum);
          jwtUtil.addJwtToHeader(newAccessToken, res);
        }

        setAuthentication(username);
      }

      // refresh 토큰이 완전하지 않다면
      else {

        throw new GlobalException(HttpStatus.UNAUTHORIZED, "Refresh 토큰 문제로 로그인 해주시길 바랍니다. ");
      }
    }
    filterChain.doFilter(req, res);
  }

  private Claims getAccessClaims(String accessToken) {

    // accessToken이 만료되던 만료되지 않던 Claims 에서 값 가져오기
    try{
      return jwtUtil.getUserInfoFromToken(accessToken);
    }catch (ExpiredJwtException e){
      return e.getClaims();
    }
  }

  public void setAuthentication(String username) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(username);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }

  private Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}