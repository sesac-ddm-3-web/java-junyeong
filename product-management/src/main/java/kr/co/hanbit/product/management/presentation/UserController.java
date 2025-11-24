package kr.co.hanbit.product.management.presentation;

import kr.co.hanbit.product.management.presentation.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  //  로그인
  public ResponseEntity<AuthResponse> signIn(SignInRequest request) {
    AuthResponse response =  userService.signIn(request);

      return ResponseEntity.ok(response);
  }

  //  회원가입
  public ResponseEntity<AuthResponse> signUp(SignupRequest request) {
    AuthResponse response =  userService.signUp(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
