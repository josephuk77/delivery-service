package com.sparta.delivery.aconfig;

import com.sparta.delivery.jwt.JwtAccessDeniedHandler;
import com.sparta.delivery.jwt.JwtAuthenticationEntryPoint;
import com.sparta.delivery.jwt.JwtAuthenticationFilter;
import com.sparta.delivery.jwt.JwtAuthorizationFilter;
import com.sparta.delivery.jwt.JwtExceptionFilter;
import com.sparta.delivery.jwt.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtUtil jwtUtil;
  private final AuthenticationConfiguration authenticationConfiguration;

  private final JwtExceptionFilter jwtExceptionFilter;
  private final JwtAuthorizationFilter jwtAuthorizationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final RedisTemplate<String, String> redisTemplate;

  private static final String GET = HttpMethod.GET.name();
  private static final String POST = HttpMethod.POST.name();

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil,
        jwtAuthenticationEntryPoint, redisTemplate);
    filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    return filter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf((csrf) -> csrf.disable());

    http.sessionManagement((sessionManagement) ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    http.authorizeHttpRequests((authorizeHttpRequests) ->
        authorizeHttpRequests
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers(publicEndPoints()).permitAll()
            .anyRequest().authenticated()
    );
    http.addFilterBefore(jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthorizationFilter, JwtAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling((exceptionHandling) -> exceptionHandling
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler));

    return http.build();
  }

  private RequestMatcher publicEndPoints() {
    List<RequestMatcher> matchers = List.of(
        // 회원가입, 로그인
        new AntPathRequestMatcher("/users/signup/**", POST),
        new AntPathRequestMatcher("/users/login", POST),

        // 조회, 검색
        new AntPathRequestMatcher("/stores/**", GET),
        new AntPathRequestMatcher("/foods/**", GET),
        new AntPathRequestMatcher("/reviews/**", GET),

        // Swagger
        new AntPathRequestMatcher("/v3/api-docs/**"),
        new AntPathRequestMatcher("/swagger-ui/**"),
        new AntPathRequestMatcher("/swagger-ui.html")
    );
    return new OrRequestMatcher(matchers);
  }
}
