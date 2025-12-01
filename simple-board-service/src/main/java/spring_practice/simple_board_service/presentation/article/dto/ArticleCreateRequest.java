package spring_practice.simple_board_service.presentation.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_practice.simple_board_service.domain.article.Article;

@Getter
@NoArgsConstructor
public class ArticleCreateRequest {

  private String title;
  private String content;

  public Article toEntity() {
    return new Article(this.getTitle(), this.getContent());
  }
}
