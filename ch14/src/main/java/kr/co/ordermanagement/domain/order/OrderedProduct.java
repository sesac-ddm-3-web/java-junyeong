package kr.co.ordermanagement.domain.order;

import kr.co.ordermanagement.domain.product.Product;
import lombok.Getter;

@Getter
public class OrderedProduct {

  private String name;
  private Integer price;
  private Integer amount;

  public OrderedProduct(Product product) {
    this.name = product.getName();
    this.price = product.getPrice();
    this.amount = product.getAmount();
  }

}
