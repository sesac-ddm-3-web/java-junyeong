package spring_junyeong.hackathon.global.exception;

public class LinkMetadataFetchException extends RuntimeException {

  public LinkMetadataFetchException() {
    super("링크 메타데이터를 가져오는 중 오류가 발생했습니다.");
  }

  public LinkMetadataFetchException(String message) {
    super(message);
  }

  public LinkMetadataFetchException(String message, Throwable cause) {
    super(message, cause);
  }
}