package spring_practice.simple_board_service.presentation.article;

import java.time.LocalDateTime;
import lombok.Getter;
import spring_practice.simple_board_service.domain.Article;

@Getter
public class ArticleResponse {

  private Long id;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public ArticleResponse(Article article) {
    this.id = article.getId();
    this.title = article.getTitle();
    this.content = article.getContent();
    this.createdAt = article.getCreatedAt();
    this.updatedAt = article.getUpdatedAt();
  }

}
