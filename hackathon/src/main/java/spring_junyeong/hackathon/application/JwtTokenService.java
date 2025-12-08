package spring_junyeong.hackathon.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.domain.TokenService;
import spring_junyeong.hackathon.global.config.JwtProperties;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

  private final JwtProperties jwtProperties;
  private Key key; // JWT 서명에 사용할 키

  /**
   * 서버 시작 시 주입받은 비밀 키 문자열을 안전한 Key 객체로 변환합니다.
   */
  @PostConstruct
  private void init() {
    // Base64 디코딩을 통해 Secret 문자열을 바이트 배열로 변환
    byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
    // 바이트 배열을 기반으로 Key 객체 생성
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * 1. Access Token을 발행합니다.
   */
  @Override
  public String issueToken(Long userId) {

    // 1. 토큰 만료 시간 설정
    Date now = new Date();
    Date validity = new Date(now.getTime() + jwtProperties.expirationTime());

    // 2. 토큰 빌드 및 반환
    return Jwts.builder()
        // 💡 수정 지점: setClaims() 대신 setSubject()와 claim()을 빌더 체인에 직접 연결
        .setSubject(String.valueOf(userId)) // Subject 클레임 설정
        // .claim("userId", userId) // 필요하다면 claim() 메서드로 커스텀 클레임 추가

        .setIssuedAt(now) // 발행 시간
        .setExpiration(validity) // 만료 시간
        .signWith(key, SignatureAlgorithm.HS256) // 서명
        .compact(); // 토큰 문자열 생성
  }

  /**
   * 2. 토큰의 유효성을 검증합니다.
   */
  @Override
  public Boolean validateToken(String token) {
    try {
      // 토큰을 파싱하면서 서명 검증 및 만료 시간 검증을 동시에 수행합니다.
      Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SignatureException | MalformedJwtException e) {
      // 서명이 유효하지 않거나 토큰 형식이 잘못됨
      return false;
    } catch (ExpiredJwtException e) {
      // 토큰이 만료됨
      return false;
    } catch (Exception e) {
      // 기타 오류
      return false;
    }
  }

  /**
   * 3. 토큰에서 사용자 ID를 추출합니다.
   */
  @Override
  public Long getUserIdFromToken(String token) {
    // 유효성 검증을 통과한 토큰이라고 가정하고 클레임을 가져옵니다.
    Claims claims = Jwts.parser()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody(); // 클레임(payload) 획득

    // Subject에 저장된 userId (String)를 Long으로 변환하여 반환합니다.
    return Long.parseLong(claims.getSubject());
  }
}