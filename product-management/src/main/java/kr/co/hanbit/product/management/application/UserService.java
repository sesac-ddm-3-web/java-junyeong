package kr.co.hanbit.product.management.application;

import kr.co.hanbit.product.management.domain.EntityNotFoundException;
import kr.co.hanbit.product.management.domain.InvalidPasswordException;
import kr.co.hanbit.product.management.domain.User;
import kr.co.hanbit.product.management.presentation.dto.AuthResponse;
import kr.co.hanbit.product.management.presentation.dto.SignInRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public AuthResponse signIn(SignInRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("User를 찾지 못했습니다."));

    if (!user.isPasswordEqual(request.getPassword())) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
    }

    return new AuthResponse(createAccessToken(user));

  }

  public String createAccessToken(User user) {

  }

  ;


}
