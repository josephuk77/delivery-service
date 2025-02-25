package com.sparta.delivery.jwt;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.user.entity.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  private final String AUTHORIZATION_KEY = "auth";
  private final String BEARER_PREFIX = "Bearer ";
  private final Long ACCESS_TIME =  60 * 60 * 1000L;
  private final Long REFRESH_TIME = 60 * 60 * 24 * 7 * 1000L;


  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String createAccessToken(String username, UserRoleEnum role) {
    Date date = new Date();

    return BEARER_PREFIX + Jwts.builder()
        .setSubject(username)
        .claim(AUTHORIZATION_KEY, role)
        .setExpiration(new Date(date.getTime() + ACCESS_TIME))
        .setIssuedAt(date)
        .signWith(key, signatureAlgorithm)
        .compact();
  }

  public String createRefreshToken() {
    Date date = new Date();

    return Jwts.builder()
            .setExpiration(new Date(date.getTime() + REFRESH_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  public void addJwtToHeader(String token, HttpServletResponse res) {
    res.setHeader(AUTHORIZATION_HEADER, token);
  }

  public String substringToken(String token) {
    if (token.startsWith(BEARER_PREFIX)) {
      return token.substring(BEARER_PREFIX.length());
    }
    throw new GlobalException(HttpStatus.BAD_REQUEST, "토큰이 올바르지 않습니다.");
  }

  public void validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      throw new GlobalException(HttpStatus.UNAUTHORIZED,
          "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (UnsupportedJwtException e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST,
          "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST,
          "JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
  }

  public boolean checkValidatedAndExpiredToken(String token) {
    try{
      validateToken(token);
    }catch(ExpiredJwtException e){
//      logger.info("===========token expired==============");
      return false;
    }
    return true;
  }

  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  public String getTokenFromRequest(HttpServletRequest req) {
    return req.getHeader(AUTHORIZATION_HEADER);
  }

}
