package spring_practice.simple_board_service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.domain.User;
import spring_practice.simple_board_service.domain.UserRepository;
import spring_practice.simple_board_service.exception.InvalidPasswordException;
import spring_practice.simple_board_service.exception.UserAlreadyExistException;
import spring_practice.simple_board_service.exception.UserNotFoundException;
import spring_practice.simple_board_service.presentation.auth.AccessToken;
import spring_practice.simple_board_service.presentation.auth.SignInRequest;
import spring_practice.simple_board_service.presentation.auth.SignupRequest;

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
