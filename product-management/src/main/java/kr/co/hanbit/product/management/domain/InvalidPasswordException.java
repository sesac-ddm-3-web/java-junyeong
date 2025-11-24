package kr.co.hanbit.product.management.domain;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException(String message) {
    super(message);
  }
}
