package spring_junyeong.hackathon.presentation.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
  public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest request) {
    AuthResponse response = authService.signIn(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
  public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignupRequest request) {
    AuthResponse response = userService.createUser(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
