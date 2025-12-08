package spring_junyeong.hackathon.presentation.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.UserService;
import spring_junyeong.hackathon.domain.User;
import spring_junyeong.hackathon.presentation.user.dto.CheckEmailRequest;
import spring_junyeong.hackathon.presentation.user.dto.CheckEmailResponse;
import spring_junyeong.hackathon.presentation.user.dto.UserResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal User user) {
    UserResponse response = userService.findUser(user.getId());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/check-email")
  public ResponseEntity<CheckEmailResponse> checkUserEmail(
      @Valid @RequestBody CheckEmailRequest request) {

    String checkEmail = request.getEmail();
    CheckEmailResponse response = userService.checkEmail(checkEmail);

    return ResponseEntity.ok(response);
  }

}