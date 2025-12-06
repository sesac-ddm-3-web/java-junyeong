package spring_junyeong.hackathon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.domain.AuthStatus;
import spring_junyeong.hackathon.domain.TokenService;
import spring_junyeong.hackathon.domain.User;
import spring_junyeong.hackathon.global.exception.AccessDeniedException;
import spring_junyeong.hackathon.global.exception.InvalidPasswordException;
import spring_junyeong.hackathon.infrastructure.AuthRepository;
import spring_junyeong.hackathon.infrastructure.UserRepository;
import spring_junyeong.hackathon.presentation.auth.dto.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthRepository authRepository;
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  public AuthResponse createAuth(User user) {
    AuthStatus authStatus = new AuthStatus(user);
    String token = authRepository.add(authStatus);
    return new AuthResponse(token);
  }

  public AuthResponse authenticateAndIssueToken(String email, String password) {
    // 1. User 조회 (UserRepository 사용)
    User user = userRepository.findByEmail(email);

    // 2. Password 검증
    if (!passwordEncoder.matches(password, user.getHashedPassword())) {
      throw new InvalidPasswordException("비밀번호 불일치");
    }

    // 3. Token 발행 (JwtService 사용)
    return new AuthResponse(tokenService.issueToken(user.getId()));

  }

  public AuthResponse getAuth(User user) {
    AuthStatus status = authRepository.getStatus(user);
    return new AuthResponse(status.getToken());
  }

  public void revokeAuth(User user) {
    authRepository.revoke(user);
  }

  private void checkAccess(User user) {
    AuthStatus status = authRepository.getStatus(user);
    Boolean isValid = status.checkAccess();
    if (!isValid) {
      throw new AccessDeniedException("유효하지 않거나 만료된 토큰입니다. 다시 로그인해주세요.");
    }
  }
}
