package spring_practice.simple_board_service.presentation.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring_practice.simple_board_service.application.UserService;

@RestController
@RequiredArgsConstructor
public class UserRestController {

  private final UserService userService;

  // 로그인
  @RequestMapping(value = "/users/sing-in", method = RequestMethod.POST)
  public ResponseEntity<UserResponse> signIn(@Valid @RequestBody SignInRequest request) {
    UserResponse response = userService.signin(request);

    return ResponseEntity.ok(response);
  }

  // 회원가입
  @RequestMapping(value = "/users/sign-up", method = RequestMethod.POST)
  public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignupRequest request) {
    UserResponse response = userService.signup(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
