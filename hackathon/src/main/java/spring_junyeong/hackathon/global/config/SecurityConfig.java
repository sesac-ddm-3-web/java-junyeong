package spring_junyeong.hackathon.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 💡 힌트: PasswordEncoder 인터페이스의 가장 흔하게 사용되는 구현체를 반환하세요.
    // 이는 Bcrypt 해싱 알고리즘을 사용합니다.
    return new BCryptPasswordEncoder(); // 👈 이 부분을 완성해보세요!
  }

}
