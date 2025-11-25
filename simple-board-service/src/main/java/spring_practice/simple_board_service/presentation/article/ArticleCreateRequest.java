package spring_practice.simple_board_service.presentation.article;

import java.time.LocalDateTime;
import lombok.Getter;
import spring_practice.simple_board_service.domain.Article;

@Getter
public class ArticleCreateRequest {

  private Long userId;
  private Long articleId;
  private String title;
  private String content;
  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = null;

  public Article toEntity() {
    return new Article(this.getArticleId(), this.getUserId(), this.getTitle(), this.getContent(),
        this.getCreatedAt(), this.getUpdatedAt());
  }
}
