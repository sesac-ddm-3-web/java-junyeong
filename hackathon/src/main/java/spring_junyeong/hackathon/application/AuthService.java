package spring_junyeong.hackathon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.domain.AuthStatus;
import spring_junyeong.hackathon.domain.User;
import spring_junyeong.hackathon.global.exception.AccessDeniedException;
import spring_junyeong.hackathon.global.exception.InvalidPasswordException;
import spring_junyeong.hackathon.global.exception.UserNotFoundException;
import spring_junyeong.hackathon.infrastructure.AuthRepository;
import spring_junyeong.hackathon.infrastructure.UserRepository;
import spring_junyeong.hackathon.presentation.auth.dto.AuthResponse;
import spring_junyeong.hackathon.presentation.auth.dto.SignInRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthRepository authRepository;
  private final UserRepository userRepository;

  public AuthResponse createAuth(User user) {
    AuthStatus authStatus = new AuthStatus(user);
    String token = authRepository.add(authStatus);
    return new AuthResponse(token);
  }

  public AuthResponse signIn(SignInRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("[" + email + "] 에 해당하는 유저를 찾지 못했습니다."));

    boolean isPasswordRight = user.checkPassword(password);
    if (!isPasswordRight) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
    }

    AuthStatus newStatus = authRepository.renew(user);

    return new AuthResponse(newStatus.getToken());

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
