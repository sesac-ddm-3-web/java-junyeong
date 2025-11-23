package spring_practice.simple_board_service.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Article {

  @Setter
  private Long id;
  @Setter
  private Long authorId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
}
