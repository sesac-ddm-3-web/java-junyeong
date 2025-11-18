package kr.co.ordermanagement.presentation.dto;

import kr.co.ordermanagement.domain.order.Order;
import lombok.Getter;

@Getter
public class OrderCreateRequest {
  private Long id;
  private Integer amount;
}
