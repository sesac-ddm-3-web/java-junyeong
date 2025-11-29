package spring_practice.simple_board_service.presentation.article;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticlesResponse {

  private List<ArticleResponse> articles;

  public ArticlesResponse(List<ArticleResponse> articles) {
    this.articles = articles;
  }
}
