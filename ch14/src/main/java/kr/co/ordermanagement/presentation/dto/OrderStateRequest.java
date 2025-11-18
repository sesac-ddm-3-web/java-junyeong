package kr.co.ordermanagement.presentation.dto;

import kr.co.ordermanagement.domain.order.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStateRequest {
  OrderStatus state;

}
