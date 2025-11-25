package kr.co.hanbit.product.management.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductDto {

  private Long id;

  @NotNull
  private String name;

  @NotNull
  private Integer price;

  @NotNull
  private Integer amount;

  public ProductDto(String name, int price, int amount) {
    this.name = name;
    this.price = price;
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getAmount() {
    return amount;
  }

}
