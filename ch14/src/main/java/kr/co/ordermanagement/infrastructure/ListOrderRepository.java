package kr.co.ordermanagement.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderRepository;
import kr.co.ordermanagement.domain.order.OrderStatus;
import org.springframework.stereotype.Repository;

@Repository
public class ListOrderRepository implements OrderRepository {
  private List<Order> orderList = new CopyOnWriteArrayList<>();

  public Order add(Order order) {
    orderList.add(order);
    return order;
  }

  public Optional<Order> findById(Long orderId) {
    return orderList.stream().filter(order -> order.sameId(orderId)).findFirst();
  }

  public List<Order> findByStatus(OrderStatus orderStatus) {
    return orderList.stream().filter(order -> order.sameState(orderStatus)).toList();
  }

}
