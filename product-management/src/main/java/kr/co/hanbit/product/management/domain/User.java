package kr.co.hanbit.product.management.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
  @Setter
  private Long id;
  private String name;
  private String email;
  private String password;

  public Boolean isPasswordEqual(String password) {
      return this.password.equals(password);
  };
}
