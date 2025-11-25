package kr.co.hanbit.product.management.domain.exception;

public class EntityAlreadyExistException extends RuntimeException {

  public EntityAlreadyExistException(String message) {
    super(message);
  }
}
