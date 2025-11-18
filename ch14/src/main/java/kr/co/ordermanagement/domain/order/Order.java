package kr.co.ordermanagement.domain.order;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Order {

  @Setter
  private Long id;
  private List<OrderedProduct> orderedProducts;
  private Double totalPrice;
  @Setter
  private OrderStatus state;

  public Order() {
    this.orderedProducts = new CopyOnWriteArrayList<>();
    this.totalPrice = 0.0;
    this.state = OrderStatus.CREATED;
  }

  public void stateToCancel() {
    if(this.state.equals(OrderStatus.CREATED)) this.state = OrderStatus.CANCELED;
    else throw new IllegalStateException("이미 취소되었거나 취소할 수 없는 주문상태입니다.");
  }

  public Boolean sameId(Long orderId) {
    return id.equals(orderId);
  }

  public Boolean sameState(OrderStatus orderStatus) {
    return state.equals(orderStatus);
  }

  public OrderedProduct addOrderedProduct(OrderedProduct product) {
    orderedProducts.add(product);
    this.totalPrice += product.getPrice();
    return product;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(id, order.id);
  }
}
