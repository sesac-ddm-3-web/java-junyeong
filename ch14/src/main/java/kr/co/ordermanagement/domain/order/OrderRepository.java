package kr.co.ordermanagement.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

  public Order add(Order order);

  public Optional<Order> findById(Long orderId);

  public List<Order> findByState(State state);
}
