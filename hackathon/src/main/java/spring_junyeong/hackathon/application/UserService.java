package spring_junyeong.hackathon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.domain.AuthStatus;
import spring_junyeong.hackathon.domain.User;
import spring_junyeong.hackathon.global.exception.UserNotFoundException;
import spring_junyeong.hackathon.infrastructure.AuthRepository;
import spring_junyeong.hackathon.presentation.auth.dto.AuthResponse;
import spring_junyeong.hackathon.presentation.user.dto.CheckEmailResponse;
import spring_junyeong.hackathon.presentation.user.dto.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AuthRepository authRepository;
  private final AuthService authService;

  public AuthResponse registerUser(String email, String name, String password
  ) {
//    @TODO
    // 1. user가 이미 존재하는지 확인
    // 2. 비밀번호를 해싱
    // 3. User 엔티티 생성
    // 4. DB에 영속화
    // 5. UserId를 반환

    User user = new User(email, name, password);
    userRepository.add(user);

    return authService.createAuth(user);
  }

  public UserResponse findUser(String token) {
    AuthStatus status = validateUserIsAlive(token);
    Long userId = status.getUserId();
    User user = userRepository.findById(userId);

    return new UserResponse(user);
  }

  public CheckEmailResponse checkEmail(String email, String token) {
    validateUserIsAlive(token);

    boolean isEmailExist = userRepository.findAll().stream()
        .anyMatch(finduser -> finduser.checkEmail(email));

    return new CheckEmailResponse(isEmailExist);
  }

  private AuthStatus validateUserIsAlive(String token) {
    return authRepository.findAll().stream().filter(auth -> auth.isTokenEqual(token))
        .findFirst().orElseThrow(() -> new UserNotFoundException("user를 찾지 못했습니다."));
  }
}
