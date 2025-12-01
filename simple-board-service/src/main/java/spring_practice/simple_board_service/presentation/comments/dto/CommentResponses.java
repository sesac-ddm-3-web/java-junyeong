package spring_practice.simple_board_service.presentation.comments.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommentResponses {

  private List<CommentResponse> comments;

  public CommentResponses(List<CommentResponse> comments) {
    this.comments = comments;
  }

}
