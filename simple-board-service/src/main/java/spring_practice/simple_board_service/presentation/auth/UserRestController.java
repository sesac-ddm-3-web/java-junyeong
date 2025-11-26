package spring_practice.simple_board_service.presentation.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring_practice.simple_board_service.application.auth.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserRestController {

  private final UserService userService;

  // 로그인
  @PostMapping("/sign-in")
  public ResponseEntity<AccessToken> signIn(@Valid @RequestBody SignInRequest request) {
    return ResponseEntity.ok(userService.signIn(request));
  }

  // 회원가입
  @PostMapping("/sign-up")
  public ResponseEntity<AccessToken> signUp(@Valid @RequestBody SignupRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request));
  }
}
