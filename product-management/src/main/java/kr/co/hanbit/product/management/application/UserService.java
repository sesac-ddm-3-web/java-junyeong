package kr.co.hanbit.product.management.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import kr.co.hanbit.product.management.domain.entity.User;
import kr.co.hanbit.product.management.domain.entity.UserRepository;
import kr.co.hanbit.product.management.domain.exception.EntityAlreadyExistException;
import kr.co.hanbit.product.management.domain.exception.EntityNotFoundException;
import kr.co.hanbit.product.management.domain.exception.InvalidPasswordException;
import kr.co.hanbit.product.management.presentation.dto.AuthResponse;
import kr.co.hanbit.product.management.presentation.dto.SignInRequest;
import kr.co.hanbit.product.management.presentation.dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final String secretKey;
  private final Long expirationTime;

  public UserService(
      UserRepository userRepository,
      @Value("${jwt.secretKey}") String secretKey,
      @Value("${jwt.expirationTime}") long expirationTime
  ) {
    this.userRepository = userRepository;
    this.secretKey = secretKey;
    this.expirationTime = expirationTime;
  }


  @Transactional
  public AuthResponse signIn(SignInRequest request) {
    User user = userRepository.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("User를 찾지 못했습니다."));

    if (!user.isPasswordEqual(request.getPassword())) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
    }

    return new AuthResponse(createAccessToken(user));

  }

  @Transactional
  public AuthResponse signUp(SignUpRequest request) {

    if (!request.getPassword().equals(request.getConfirmPassword())) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
    }

    Optional<User> user = userRepository.findUserByEmail(request.getEmail());

    if (user.isPresent()) {
      throw new EntityAlreadyExistException("이미 존재하는 ID입니다.");
    } else {
      User idNotSettedUser = request.toEntity();
      User idSettedUser = userRepository.add(idNotSettedUser);

      return new AuthResponse(createAccessToken(idSettedUser));
    }

  }

  public String createAccessToken(User user) {
    Date now = new Date();

    Date expiryDate = new Date(now.getTime() + expirationTime);

    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    // JWT 토큰 생성
    // 1. Header 설정 (알고리즘: HS256) - Jwts.builder가 기본값으로 처리
    String token = Jwts.builder()
        // 2. Payload(클레임) 설정
        .setSubject(user.getId().toString()) // 'sub' 클레임: 사용자 고유 식별자
        // ⬇️ User 도메인에 role이 없을 경우, "USER"라는 기본값(Default Role)을 설정합니다.
        .claim("role", "USER")
        .setIssuedAt(now)                        // 'iat' 클레임: 발급 시간
        .setExpiration(expiryDate)               // 'exp' 클레임: 만료 시간

        // 3. Signature 설정
        // Secret Key를 사용하여 HS256 알고리즘으로 서명
        .signWith(SignatureAlgorithm.HS256, key)

        .compact(); // 최종 토큰 문자열 생성

    return token;
  }

  public Long verifyAndGetUserId(String token) {
    try {
      // 1. Secret Key 생성 (토큰 생성 시 사용한 것과 동일해야 함)
      SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

      // 2. 토큰 파싱 및 검증
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();

      // 3. Subject (저장된 User ID) 추출
      String userIdStr = claims.getSubject();

      // 추출된 ID가 null이거나 숫자가 아니면 예외 처리
      if (userIdStr == null) {
        throw new MalformedJwtException("Token subject (User ID) is missing.");
      }

      return Long.parseLong(userIdStr);

    } catch (ExpiredJwtException e) {
      // 토큰 만료
      throw new RuntimeException("JWT token has expired.", e);
    } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
      // 잘못된 형식, 지원되지 않는 토큰, 잘못된 인자
      throw new RuntimeException("Invalid JWT token format or structure.", e);
    } catch (Exception e) {
      // 기타 모든 JWT 관련 예외 (SignatureException 포함)
      throw new RuntimeException("JWT verification failed.", e);
    }
  }


}
