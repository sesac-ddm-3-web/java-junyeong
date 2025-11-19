package kr.co.ordermanagement.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

  @NotNull(message = "id는 필수 값입니다.")
  private Long id;

  @NotNull(message = "amount는 필수 값입니다.")
  @Min(value = 1, message = "주문 수량은 1개 이상이어야 합니다.")
  private Integer amount;

}
