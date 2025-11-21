package kr.co.ordermanagement.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderRepository;
import kr.co.ordermanagement.domain.order.OrderStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("list")
public class ListOrderRepository implements OrderRepository {
  private List<Order> orderList = new CopyOnWriteArrayList<>();
  private AtomicLong orderIdGenerator = new AtomicLong(0L);

  @Override
  public Order add(Order order) {
    order.setId(orderIdGenerator.incrementAndGet());
    orderList.add(order);
    return order;
  }

  @Override
  public Optional<Order> findById(Long orderId) {
    return orderList.stream().filter(order -> order.sameId(orderId)).findFirst();
  }

  @Override
  public List<Order> findByStatus(OrderStatus orderStatus) {
    return orderList.stream().filter(order -> order.sameState(orderStatus)).toList();
  }

}
