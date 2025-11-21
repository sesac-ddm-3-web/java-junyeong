package kr.co.ordermanagement.presentation.dto;

import java.util.List;
import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderedProduct;
import kr.co.ordermanagement.domain.order.State;
import lombok.Getter;

@Getter
public class OrderResponse {

  private Long id;
  private List<OrderedProduct> orderedProducts;
  private Integer totalPrice;
  private State state;

  public OrderResponse(Order order) {
    this.id = order.getId();
    this.orderedProducts = order.getOrderedProducts();
    this.totalPrice = order.getTotalPrice();
    this.state = order.getState();
  }

}
