package kr.co.ordermanagement.application;

import java.util.List;
import kr.co.ordermanagement.domain.exception.EntityNotFoundException;
import kr.co.ordermanagement.domain.exception.InsufficientStockException;
import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderRepository;
import kr.co.ordermanagement.domain.order.OrderStatus;
import kr.co.ordermanagement.domain.order.OrderedProduct;
import kr.co.ordermanagement.domain.product.Product;
import kr.co.ordermanagement.domain.product.ProductRepository;
import kr.co.ordermanagement.presentation.dto.OrderCreateRequest;
import kr.co.ordermanagement.presentation.dto.OrderResponse;
import kr.co.ordermanagement.presentation.dto.OrderStateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleOrderService {

  private ProductRepository productRepository;
  private OrderRepository orderRepository;

  @Autowired
  public SimpleOrderService(ProductRepository productRepository, OrderRepository orderRepository) {
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
  }

  public OrderResponse createOrder(List<OrderCreateRequest> orderCreateRequests) {

    validateOrder(orderCreateRequests);

    Order newOrder = new Order();

    orderCreateRequests.forEach(orderCreateRequest -> {
      Long productId = orderCreateRequest.getId();
      Product product = productRepository.findById(productId);

      OrderedProduct orderedProduct = new OrderedProduct(product);

      newOrder.addOrderedProduct(orderedProduct);
      product.decreaseAmount(orderCreateRequest.getAmount());
    });

    Order order = orderRepository.add(newOrder);

    return new OrderResponse(order);

  }

  public OrderResponse updateOrderState(Long orderId, OrderStateRequest request) {

    Order order = orderRepository.findById(orderId).orElseThrow(
        () -> new EntityNotFoundException(String.format("%d번 주문을 찾지 못했습니다.", orderId)));
    order.setState(request.getState());
    return new OrderResponse(order);
  }


  public OrderResponse getOrderInfo(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Order를 찾지 못했습니다.")));
    return new OrderResponse(order);
  }


  public List<OrderResponse> getOrdersByState(OrderStatus status) {
    List<Order> ordersFilteredStatus = orderRepository.findByStatus(status);
    return ordersFilteredStatus.stream().map(OrderResponse::new).toList();
  }

  public OrderResponse cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(()-> new EntityNotFoundException("Order를 찾지 못했습니다."));
    order.stateToCancel();
    return new OrderResponse(order);
  }

  private void validateOrder(List<OrderCreateRequest> orderCreateRequests)
      throws InsufficientStockException, EntityNotFoundException {

    for (OrderCreateRequest request : orderCreateRequests) {
      Long productId = request.getId();
      Integer amount = request.getAmount();
      if (amount == null) {
        throw new IllegalArgumentException("주문 수량(amount)은 필수 값입니다. ");
      }
      Product product = productRepository.findById(productId);
      if (product.getAmount() < amount) {
        throw new InsufficientStockException(String.format("%d번 상품의 수량이 부족합니다.", productId));
      }
    }
  }
}
