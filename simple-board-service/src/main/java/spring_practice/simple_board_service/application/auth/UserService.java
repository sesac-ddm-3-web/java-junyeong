package spring_practice.simple_board_service.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.common.exception.InvalidPasswordException;
import spring_practice.simple_board_service.common.exception.UserAlreadyExistException;
import spring_practice.simple_board_service.common.exception.UserNotFoundException;
import spring_practice.simple_board_service.domain.auth.User;
import spring_practice.simple_board_service.domain.auth.UserRepository;
import spring_practice.simple_board_service.presentation.auth.dto.AccessToken;
import spring_practice.simple_board_service.presentation.auth.dto.SignInRequest;
import spring_practice.simple_board_service.presentation.auth.dto.SignupRequest;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final TokenService tokenService;

  public AccessToken signIn(SignInRequest request) {

    String email = request.getEmail();
    String password = request.getPassword();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException());

    if (!user.isPasswordEqual(password)) {
      throw new InvalidPasswordException();
    }

    return tokenService.generateToken(user);
  }

  public AccessToken signUp(SignupRequest request) {

    String email = request.getEmail();

    validateUserExistence(email);

    User user = request.toEntity();

    userRepository.add(user);

    return tokenService.generateToken(user);
  }

  private void validateUserExistence(String email) {
    userRepository
        .findByEmail(email)
        .ifPresent(
            user -> {
              throw new UserAlreadyExistException();
            });
  }
}
