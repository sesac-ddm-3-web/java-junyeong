package spring_practice.simple_board_service.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
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
import spring_practice.simple_board_service.domain.User;
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
}
