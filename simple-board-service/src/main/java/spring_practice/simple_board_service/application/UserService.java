package spring_practice.simple_board_service.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.domain.User;
import spring_practice.simple_board_service.domain.UserRepository;
import spring_practice.simple_board_service.exception.InvalidPasswordException;
import spring_practice.simple_board_service.exception.UserAlreadyExistException;
import spring_practice.simple_board_service.exception.UserNotFoundException;
import spring_practice.simple_board_service.presentation.user.SignInRequest;
import spring_practice.simple_board_service.presentation.user.SignupRequest;
import spring_practice.simple_board_service.presentation.user.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse signin(SignInRequest request) {

    User findUser =
        validateUserIsExist(request.getEmail()).orElseThrow(() -> new UserNotFoundException());
    validatePasswordIsEqual(findUser, request.getPassword());

    return new UserResponse(findUser);
  }

  public UserResponse signup(SignupRequest request) {
    userRepository
        .findByEmail(request.getEmail())
        .ifPresent(
            user -> {
              throw new UserAlreadyExistException();
            });

    User user =
        userRepository.add(new User(request.getName(), request.getEmail(), request.getPassword()));

    return new UserResponse(user);
  }

  private Optional<User> validateUserIsExist(String email) {
    return userRepository.findByEmail(email);
  }

  private void validatePasswordIsEqual(User user, String password) {
    if (user.isPasswordEqual(password) == false) throw new InvalidPasswordException();
  }
}
