package spring_practice.simple_board_service.presentation.article.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleUpdateRequest {

  @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
  private String title;

  @Size(max = 3000, message = "내용은 3000자를 초과할 수 없습니다.")
  private String content;

}