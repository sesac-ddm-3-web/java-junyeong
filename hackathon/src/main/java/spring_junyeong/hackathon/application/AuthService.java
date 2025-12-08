package spring_junyeong.hackathon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_junyeong.hackathon.domain.TokenService;
import spring_junyeong.hackathon.domain.User;
import spring_junyeong.hackathon.domain.UserRepository;
import spring_junyeong.hackathon.global.exception.InvalidPasswordException;
import spring_junyeong.hackathon.global.exception.UserIsExistException;
import spring_junyeong.hackathon.global.exception.UserNotFoundException;
import spring_junyeong.hackathon.presentation.auth.dto.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public AuthResponse signIn(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(email));

    if (!passwordEncoder.matches(password, user.getHashedPassword())) {
      throw new InvalidPasswordException("비밀번호 불일치");
    }

    return new AuthResponse(tokenService.issueToken(user.getId()));
  }

  @Transactional
  public Long signUp(String email, String name, String password
  ) {
    userRepository.findByEmail(email).ifPresent(user -> {
      throw new UserIsExistException("이미 존재하는 사용자 이메일입니다.");
    });

    User user = User.builder()
        .email(email)
        .name(name)
        .hashedPassword(passwordEncoder.encode(password))
        .build();

    user = userRepository.save(user);

    return user.getId();
  }
}
