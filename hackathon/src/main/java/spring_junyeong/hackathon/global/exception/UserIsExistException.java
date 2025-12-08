package spring_junyeong.hackathon.global.exception; // 적절한 예외 패키지 지정

public class UserIsExistException extends RuntimeException {

  public UserIsExistException() {
    super("이미 존재하는 사용자입니다.");
  }

  public UserIsExistException(String message) {
    super(message);
  }

  public UserIsExistException(String message, Throwable cause) {
    super(message, cause);
  }
}