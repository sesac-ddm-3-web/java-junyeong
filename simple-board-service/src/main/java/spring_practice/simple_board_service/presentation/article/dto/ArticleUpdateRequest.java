package spring_practice.simple_board_service.presentation.article;

import lombok.Getter;

@Getter
public class ArticleUpdateRequest {

  private Long articleId;
  private String title;
  private String content;


}