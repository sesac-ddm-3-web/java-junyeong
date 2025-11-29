package spring_practice.simple_board_service.presentation.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPrincipal {

  private final Long userId;

}