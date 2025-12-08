package spring_junyeong.hackathon.presentation.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.AuthService;
import spring_junyeong.hackathon.presentation.auth.dto.AuthResponse;
import spring_junyeong.hackathon.presentation.auth.dto.SignInRequest;
import spring_junyeong.hackathon.presentation.auth.dto.SignupRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-in")
  public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest request) {
    AuthResponse response = authService.signIn(request.email(),
        request.password());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/sign-up")
  public ResponseEntity<Long> signUp(@Valid @RequestBody SignupRequest request) {
    Long userId = authService.signUp(request.email(), request.name(),
        request.password());

    return ResponseEntity.status(HttpStatus.CREATED).body(userId);
  }
}
