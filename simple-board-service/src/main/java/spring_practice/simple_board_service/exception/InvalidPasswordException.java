package spring_practice.simple_board_service.exception;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException() {
    super("비밀번호가 일치하지 않습니다.");
  }
}
