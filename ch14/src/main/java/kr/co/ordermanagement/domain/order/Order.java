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
  private Integer totalPrice;
  @Setter
  private State state;

  public Order() {
    this.orderedProducts = new CopyOnWriteArrayList<>();
    this.totalPrice = 0;
    this.state = State.CREATED;
  }

  public Order(List<OrderedProduct> orderedProducts) {
    this.orderedProducts = orderedProducts;
    this.totalPrice = orderedProducts.stream().mapToInt(OrderedProduct::getPrice).sum();
    this.state = State.CREATED;
  }

  public void stateToCancel() {
    if (this.state.equals(State.CREATED)) {
      this.state = State.CANCELED;
    } else {
      throw new IllegalStateException("이미 취소되었거나 취소할 수 없는 주문상태입니다.");
    }
  }

  public void changeStateForce(State state) {
    this.state = state;
  }

  public Boolean sameId(Long orderId) {
    return id.equals(orderId);
  }

  public Boolean sameState(State state) {
    return this.state.equals(state);
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
