package spring_practice.simple_board_service.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("User를 찾지 못했습니다.");
  }
}
