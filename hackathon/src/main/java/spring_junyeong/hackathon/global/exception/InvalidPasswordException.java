package spring_junyeong.hackathon.global.exception;

public class InvalidPasswordException extends RuntimeException {

  private static final int HTTP_STATUS_CODE = 401;

  public InvalidPasswordException(String message) {
    super(message);
  }

  public int getHttpStatusCode() {
    return HTTP_STATUS_CODE;
  }
}
