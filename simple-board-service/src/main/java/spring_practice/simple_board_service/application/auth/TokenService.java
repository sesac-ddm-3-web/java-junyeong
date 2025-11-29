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
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.domain.auth.User;
import spring_practice.simple_board_service.presentation.auth.dto.AccessToken;

@Service
public class TokenService {

  private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  private final Key secretKey;

  private static final long EXPIRATION_TIME_MILLIS = TimeUnit.HOURS.toMillis(1);


  public TokenService(@Value("${jwt.secret-key}") String secretKeyString) {

    if (secretKeyString == null || secretKeyString.isBlank() || secretKeyString.length() < 32) {
      throw new IllegalArgumentException(
          "JWT Secret Key (jwt.secret-key) must be set and must be at least 32 bytes long for HS256.");
    }

    byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public AccessToken generateToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MILLIS);

    String token = Jwts.builder()
        .setSubject(user.getId().toString())
        .setIssuedAt(now)
        .setExpiration(expiryDate)

        .signWith(this.secretKey, SignatureAlgorithm.HS256)
        .compact();

    return new AccessToken(token);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()

          .setSigningKey(this.secretKey)
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

    Claims claims = getClaims(token);

    String userIdString = claims.getSubject();

    if (userIdString == null || userIdString.isBlank()) {
      throw new JwtException("토큰에 유효한 사용자 ID(Subject)가 없습니다.");
    }

    return Long.parseLong(userIdString);
  }
}
