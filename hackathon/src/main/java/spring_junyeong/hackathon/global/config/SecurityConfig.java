package spring_junyeong.hackathon.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring_junyeong.hackathon.presentation.auth.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 💡 힌트: PasswordEncoder 인터페이스의 가장 흔하게 사용되는 구현체를 반환하세요.
    // 이는 Bcrypt 해싱 알고리즘을 사용합니다.
    return new BCryptPasswordEncoder(); // 👈 이 부분을 완성해보세요!
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        // 1. 인증/인가 설정 (요청별 허용 설정)
        .authorizeHttpRequests(authorize -> authorize
            // 회원가입 및 로그인 경로는 인증 없이 허용
            .requestMatchers("/api/auth/sign-up", "/api/auth/sign-in").permitAll()
            // 💡 추가된 로직: 모든 GET 요청은 인증 없이 허용 (/**는 모든 경로를 의미)
            .requestMatchers(HttpMethod.GET, "/**").permitAll()
            // 나머지 모든 요청은 반드시 인증 필요 (토큰 필요)
            .anyRequest().authenticated()
        )
        // 2. CSRF 비활성화 (REST API 환경)
        .csrf(csrf -> csrf.disable())

        // 3. 세션 관리 (STATELESS 설정)
        // JWT를 사용하므로 서버가 사용자 세션을 유지하지 않습니다.
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // 4. 사용자 정의 필터 등록
        // 구현한 JwtAuthenticationFilter를 Spring Security의 기본 인증 필터 이전에 추가합니다.
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }

}
