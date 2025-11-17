package spring_junyeong.hackathon.global.exception; // 패키지명은 프로젝트에 맞게 변경하세요.

public class AccessDeniedException extends RuntimeException {

  private static final int HTTP_STATUS_CODE = 403;

  public AccessDeniedException(String message) {
    super(message);
  }

  public int getHttpStatusCode() {
    return HTTP_STATUS_CODE;
  }
}