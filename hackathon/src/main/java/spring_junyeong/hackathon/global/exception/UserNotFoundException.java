package spring_junyeong.hackathon.global.exception;

public class UserNotFoundException extends RuntimeException {

  private static final int HTTP_STATUS_CODE = 404;

  public UserNotFoundException(String message) {
    super(message);
  }

  public int getHttpStatusCode() {
    return HTTP_STATUS_CODE;
  }

}
