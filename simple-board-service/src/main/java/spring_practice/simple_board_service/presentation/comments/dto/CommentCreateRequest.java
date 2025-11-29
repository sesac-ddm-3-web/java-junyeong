package spring_practice.simple_board_service.presentation.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_practice.simple_board_service.domain.comment.Comment;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

  @NotBlank
  @Size(min = 1, max = 100, message = "text는 반드시 1자 이상이여야 합니다.")
  private String text;

  public Comment toEntity(Long articleId, Long authorId) {
    return new Comment(articleId, authorId, this.getText());
  }

}
