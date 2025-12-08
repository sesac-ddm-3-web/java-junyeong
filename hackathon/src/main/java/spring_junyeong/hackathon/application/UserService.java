package spring_junyeong.hackathon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_junyeong.hackathon.domain.User;
import spring_junyeong.hackathon.domain.UserRepository;
import spring_junyeong.hackathon.global.exception.UserNotFoundException;
import spring_junyeong.hackathon.presentation.user.dto.CheckEmailResponse;
import spring_junyeong.hackathon.presentation.user.dto.UserResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 👈 조회 전용 트랜잭션 설정
public class UserService {

  private final UserRepository userRepository;

  public UserResponse findUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("해당 ID의 사용자를 찾을 수 없습니다."));

    return new UserResponse(user);
  }

  public CheckEmailResponse checkEmail(String email) {
    boolean isEmailExist = userRepository.existsByEmail(email);

    return new CheckEmailResponse(isEmailExist);
  }
}