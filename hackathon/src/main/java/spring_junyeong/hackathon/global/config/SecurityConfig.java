package spring_junyeong.hackathon.global.config;

import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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
    return new BCryptPasswordEncoder();
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 💡 힌트: Vercel 주소와 로컬 주소를 명시하거나, 개발 환경에서는 *을 사용합니다.
    // 실제 운영 환경에서는 보안을 위해 명시적인 출처(Origin)를 사용해야 합니다.
    configuration.setAllowedOrigins(List.of(
        "http://localhost:3000", // 프런트엔드 개발 환경
        "http://localhost:8080" // 백엔드 index.html
    ));

    // 허용할 HTTP 메서드 (GET, POST, PUT, DELETE 등)
    configuration.setAllowedMethods(List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()
    ));

    // Authorization 헤더를 포함하여 모든 헤더 허용
    configuration.setAllowedHeaders(List.of("*"));

    // 인증 정보 (쿠키, Authorization 헤더 등)를 포함하여 요청을 보낼 수 있게 허용
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 모든 경로 (/**)에 대해 위 설정 적용
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
