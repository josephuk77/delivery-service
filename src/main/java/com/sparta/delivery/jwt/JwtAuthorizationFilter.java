package com.sparta.delivery.jwt;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.repository.UserRepository;
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
import org.springframework.security.authentication.DisabledException;
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
  private final RedisTemplate<String, String> redisTemplate;
  private final UserRepository userRepository;

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

      // access token 에서 claim 과 sub 를 가져오기
      Claims accessClaims = getAccessClaims(accessToken);
      String username = accessClaims.getSubject();

      // access token claim 으로 refresh 토큰 가져오기
      String refreshToken = redisTemplate.opsForValue().get(accessClaims.getSubject());

      // refresh token 이 완전하면
      if (StringUtils.hasText(refreshToken) && jwtUtil.checkValidatedAndExpiredToken(refreshToken)) {

        // repository 에서 user 에 관한 정보를 가져옴
        User user = findUser(username);

        // access token이 만료되었다면
        if (!jwtUtil.checkValidatedAndExpiredToken(accessToken)) {

          // User에서 role 추출
          UserRoleEnum userRoleEnum = user.getRole();

          // 새로운 access token을 발급하여 헤더에 넣어줌
          String newAccessToken = jwtUtil.createAccessToken(username, userRoleEnum);
          jwtUtil.addJwtToHeader(newAccessToken, res);
        }

        setAuthentication(user);
      }

      // refresh 토큰이 완전하지 않다면
      else {

        throw new GlobalException(HttpStatus.UNAUTHORIZED, "Refresh 토큰 문제로 로그인 해주시길 바랍니다. ");
      }
    }
    filterChain.doFilter(req, res);
  }

  private User findUser(String username) {

    User user = this.userRepository.findByUsername(username).orElseThrow(() ->
            new GlobalException(HttpStatus.NO_CONTENT, "토큰에 해당하는 유저가 존재하지 않습니다. "));

    if(user.getDeletedAt() != null){
      throw new DisabledException("탈퇴한 사용자입니다.");
    }

    return user;
  }

  private Claims getAccessClaims(String accessToken) {

    // accessToken이 만료되던 만료되지 않던 Claims 에서 값 가져오기
    try{
      return jwtUtil.getUserInfoFromToken(accessToken);
    }catch (ExpiredJwtException e){
      return e.getClaims();
    }
  }

  public void setAuthentication(User user) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(user);
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  private Authentication createAuthentication(User user) {
    UserDetails userDetails = new UserDetailsImpl(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}