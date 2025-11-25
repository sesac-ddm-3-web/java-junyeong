package kr.co.hanbit.product.management.presentation.controller;

import jakarta.validation.Valid;
import kr.co.hanbit.product.management.application.UserService;
import kr.co.hanbit.product.management.presentation.dto.AuthResponse;
import kr.co.hanbit.product.management.presentation.dto.SignInRequest;
import kr.co.hanbit.product.management.presentation.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

  private final UserService userService;

  //  로그인
  @PostMapping("/sign-in")
  public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest request) {
    AuthResponse response = userService.signIn(request);

    return ResponseEntity.ok(response);
  }

  //  회원가입
  @PostMapping("/sign-up")
  public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest request) {
    AuthResponse response = userService.signUp(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
