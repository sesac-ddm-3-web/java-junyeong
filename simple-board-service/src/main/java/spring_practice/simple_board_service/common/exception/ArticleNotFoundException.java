package spring_practice.simple_board_service.common.exception;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException() {
    super("게시글을 찾지 못했습니다.");
  }

  public ArticleNotFoundException(Long articleId) {
    super("ID " + articleId + "에 해당하는 게시글을 찾지 못했습니다.");
  }

  public ArticleNotFoundException(String message) {
    super(message);
  }

}
