package spring_junyeong.hackathon.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// @ConstructorBinding은 Spring Boot 2.4 이후 버전에서 ConfigurationProperties를
// 레코드(record)에서 사용할 때 필드를 주입받기 위해 필요합니다.
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String secret,
    Long expirationTime
) {

}