package kr.co.ordermanagement.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import kr.co.ordermanagement.application.SimpleOrderService;
import kr.co.ordermanagement.domain.order.OrderStatus;
import kr.co.ordermanagement.presentation.dto.OrderCreateRequest;
import kr.co.ordermanagement.presentation.dto.OrderResponse;
import kr.co.ordermanagement.presentation.dto.OrderStateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderRestController {

  private final SimpleOrderService simpleOrderService;

  // 상품 주문 api
  @RequestMapping(value = "/orders", method = RequestMethod.POST)
  public OrderResponse order(@Valid @RequestBody List<OrderCreateRequest> orderCreateRequests) {
    return simpleOrderService.createOrder(orderCreateRequests);
  }

  // {orderId}로 주문을 조회하는 api
  @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET)
  public OrderResponse getOrderInfo(@PathVariable Long orderId) {
    return simpleOrderService.getOrderInfo(orderId);
  }

  // 특정 주문상태를 가지는 주문을 전부 조회할 수 있는 API
  @RequestMapping(value = "/orders", method = RequestMethod.GET)
  public List<OrderResponse> getOrdersByState(@RequestParam("state") OrderStatus orderStatus) {
    return simpleOrderService.getOrdersByState(orderStatus);
  }

  // {orderId}에 해당하는 주문의 상태를 강제로 변경하는 api
  @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.PATCH)
  public OrderResponse updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody OrderStateRequest orderStateRequest) {
    return simpleOrderService.updateOrderState(orderId, orderStateRequest);
  }

  // {orderId}에 해당하는 주문 취소 api
  @RequestMapping(value = "/orders/{orderId}/cancle", method = RequestMethod.PATCH)
  public OrderResponse cancelOrder(@PathVariable Long orderId) {
    return simpleOrderService.cancelOrder(orderId);
  }
}
