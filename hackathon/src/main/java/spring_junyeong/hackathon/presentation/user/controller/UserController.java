package spring_junyeong.hackathon.presentation.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.UserService;
import spring_junyeong.hackathon.global.exception.AccessDeniedException;
import spring_junyeong.hackathon.presentation.user.dto.CheckEmailRequest;
import spring_junyeong.hackathon.presentation.user.dto.CheckEmailResponse;
import spring_junyeong.hackathon.presentation.user.dto.UserResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  public ResponseEntity<UserResponse> getMyInfo(
      @RequestHeader("Authorization") String authorizationHeader) {
    String accessToken = extractAccessToken(authorizationHeader);

    UserResponse response = userService.findUser(accessToken);

    return ResponseEntity.ok(response);

  }

  @RequestMapping(value = "/users/check-email", method = RequestMethod.POST)
  public ResponseEntity<CheckEmailResponse> checkUserEmail(
      @Valid @RequestBody CheckEmailRequest request,
      @RequestHeader("Authorization") String authorizationHeader) {

    String accessToken = extractAccessToken(authorizationHeader);
    String checkEmail = request.getEmail();
    CheckEmailResponse response = userService.checkEmail(checkEmail, accessToken);

    return ResponseEntity.ok(response);
  }

  private String extractAccessToken(String authorizationHeader) {
    String accessToken;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      accessToken = authorizationHeader.substring(7);
    } else {
      // 토큰 형식이 잘못되었거나 없는 경우 403 예외 발생
      throw new AccessDeniedException("유효한 Bearer 토큰 형식이 필요합니다.");
    }

    return accessToken;
  }

}
