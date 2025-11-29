package spring_practice.simple_board_service.domain.comment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Comment {

  @Setter
  private Long id;
  @Setter
  private Long articleId;
  @Setter
  private Long authorId;
  private String text;
  private LocalDateTime createdAt;

  public Comment(Long articleId, Long authorId, String text) {
    this.articleId = articleId;
    this.authorId = authorId;
    this.text = text;
    this.createdAt = LocalDateTime.now();
  }

  public void updateText(String text) {
    this.text = text;
  }
}
