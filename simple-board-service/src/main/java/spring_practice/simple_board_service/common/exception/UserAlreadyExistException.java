package spring_practice.simple_board_service.common.exception;

public class UserAlreadyExistException extends RuntimeException {

  public UserAlreadyExistException() {
    super("이미 존재하는 email입니다.");
  }
}
