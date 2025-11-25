package spring_practice.simple_board_service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring_practice.simple_board_service.application.TokenService;
import spring_practice.simple_board_service.filter.JwtAuthenticationFilter;

@Configuration
public class AppConfig {

  private final TokenService tokenService;

  public AppConfig(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

    // 1. 필터 인스턴스 생성 및 TokenService 주입
    registrationBean.setFilter(new JwtAuthenticationFilter(tokenService));

    // 2. 모든 URL 패턴에 필터 적용
    registrationBean.addUrlPatterns("/*");

    // 3. 필터 실행 순서 지정 (가장 먼저 실행되도록 1로 설정)
    registrationBean.setOrder(1);

    return registrationBean;
  }
}

