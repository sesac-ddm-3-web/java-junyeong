package spring_practice.simple_board_service.domain.comment;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Comment {

  @Setter
  private Long id;
  @Setter
  private Long articleId;
  @Setter
  private Long authorId;
  private String text;
  private LocalDateTime createdAt;
}
