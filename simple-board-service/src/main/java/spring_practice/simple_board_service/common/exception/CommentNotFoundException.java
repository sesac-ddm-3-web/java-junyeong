package spring_practice.simple_board_service.common.exception;

public class CommentNotFoundException extends RuntimeException {

  public CommentNotFoundException() {
    super("댓글을 찾지 못했습니다.");
  }

  public CommentNotFoundException(Long id) {
    super(id + "에 해당하는 댓글을 찾지 못했습니다.");
  }

  public CommentNotFoundException(String message) {
    super(message);
  }
}