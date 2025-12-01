package spring_practice.simple_board_service.presentation.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequest {

  @NotBlank
  @Size(min = 1, max = 100, message = "text는 1자이상 100자 이하여야 합니다.")
  private String text;

}
