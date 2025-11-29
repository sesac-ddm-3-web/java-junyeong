package spring_practice.simple_board_service.presentation.comments.dto;

import lombok.Getter;

@Getter
public class CommentRequest {

  private String text;

  // @Warn - text를 controller에서 받을 때 해야 할 유효성 검사는 없을까? -> xxs, csrf, sql injection 상관없나?
}
