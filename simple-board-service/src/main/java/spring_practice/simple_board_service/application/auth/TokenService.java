package spring_practice.simple_board_service.application.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.domain.auth.User;
import spring_practice.simple_board_service.presentation.auth.AccessToken;

@Service
public class TokenService {

  private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  private static final String SECRET_KEY_STRING = System.getenv("JWT_SECRET_KEY");
  private final Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

  private static final long EXPIRATION_TIME_MILLIS = TimeUnit.HOURS.toMillis(1);

  public AccessToken generateToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MILLIS);

    String token = Jwts.builder()
        .setSubject(user.getId().toString())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();

    return new AccessToken(token);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      logger.error("Invalid JWT signature: {}", ex.getMessage());
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token: {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token: {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token: {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty or null: {}", ex.getMessage());
    }
    return false;
  }

  public Claims getClaims(String token) {
    Jws<Claims> claimsJws = Jwts.parser()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token);

    return claimsJws.getBody();
  }

  public Long getUserIdFromToken(String token) {
    // 1. 토큰 유효성 검사 (validateToken과 로직 중복 피하기 위해 validate를 여기서 사용하거나,
    //    getClaims에서 발생하는 예외에 의존할 수 있습니다. 여기서는 getClaims 사용)
    Claims claims = getClaims(token);

    // 2. Subject(ID)를 가져와 Long으로 변환합니다.
    String userIdString = claims.getSubject();

    // 3. Optional: null 체크나 빈 문자열 체크 추가
    if (userIdString == null || userIdString.isBlank()) {
      throw new JwtException("토큰에 유효한 사용자 ID(Subject)가 없습니다.");
    }

    // 4. Long 타입으로 변환 후 반환
    return Long.parseLong(userIdString);
  }
}
