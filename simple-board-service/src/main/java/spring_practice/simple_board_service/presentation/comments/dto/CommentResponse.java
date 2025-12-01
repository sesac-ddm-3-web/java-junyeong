package spring_practice.simple_board_service.presentation.comments.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import spring_practice.simple_board_service.domain.comment.Comment;

@Getter
public class CommentResponse {

  @Setter
  private Long id;
  private Long articleId;
  private Long authorId;
  private String text;
  private LocalDateTime createdAt;

  public CommentResponse(Comment comment) {
    this.id = comment.getId();
    this.articleId = comment.getArticleId();
    this.authorId = comment.getAuthorId();
    this.text = comment.getText();
    this.createdAt = comment.getCreatedAt();
  }
}
