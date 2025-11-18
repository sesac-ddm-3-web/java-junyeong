package kr.co.ordermanagement.presentation.dto;

import jakarta.validation.constraints.NotNull;
import kr.co.ordermanagement.domain.order.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStateRequest {
  @NotNull(message = "CREATED, SHIPPING, COMPLETED, CANCELED 중 하나여야 합니다.")
  OrderStatus state;

}
