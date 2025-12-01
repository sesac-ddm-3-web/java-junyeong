package spring_practice.simple_board_service.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring_practice.simple_board_service.application.auth.TokenService;
import spring_practice.simple_board_service.common.filter.JwtAuthenticationFilter;

@Configuration
public class AppConfig {

  private final TokenService tokenService;

  public AppConfig(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new JwtAuthenticationFilter(tokenService));

    registrationBean.addUrlPatterns("/*");

    registrationBean.setOrder(1);

    return registrationBean;
  }
}

