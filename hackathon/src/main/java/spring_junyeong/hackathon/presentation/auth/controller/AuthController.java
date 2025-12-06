package spring_junyeong.hackathon.presentation.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.AuthService;
import spring_junyeong.hackathon.application.UserService;
import spring_junyeong.hackathon.presentation.auth.dto.AuthResponse;
import spring_junyeong.hackathon.presentation.auth.dto.SignInRequest;
import spring_junyeong.hackathon.presentation.auth.dto.SignupRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @PostMapping("/sign-in")
  public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest request) {
    AuthResponse response = authService.authenticateAndIssueToken(request.email(), request.password());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/sign-up")
  public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignupRequest request) {
    AuthResponse response = userService.createUser(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
