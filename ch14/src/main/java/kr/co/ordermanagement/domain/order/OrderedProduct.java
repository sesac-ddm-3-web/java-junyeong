package kr.co.ordermanagement.domain.order;

import kr.co.ordermanagement.domain.product.Product;
import kr.co.ordermanagement.presentation.dto.OrderCreateRequest;
import lombok.Getter;

@Getter
public class OrderedProduct {

  private String name;
  private Integer price;
  private Integer amount;

  public OrderedProduct(Product product, OrderCreateRequest orderCreateRequest ) {
    this.name = product.getName();
    this.price = product.getPrice() * orderCreateRequest.getAmount();
    this.amount = orderCreateRequest.getAmount();
  }

}
