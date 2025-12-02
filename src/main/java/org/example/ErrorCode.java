package org.example;

public enum ErrorCode {

  // 유효성 검증 관련 오류 메시지
  EMPTY_INPUT("입력 수식은 비어있을 수 없습니다."),
  EMPTY_TOKENS("수식 형식이 잘못되었습니다. 유효한 연산 토큰이 없습니다."),

  // 패턴 검증 관련 오류 메시지
  INVALID_START("수식 형식이 잘못되었습니다. 숫자로 시작해야 합니다."),
  INVALID_END("수식 형식이 잘못되었습니다. 연산자로 끝날 수 없습니다."),
  NUMBER_AT_OPERATOR_POS("수식 형식이 잘못되었습니다. 연산자 위치에 숫자가 있습니다."),
  OPERATOR_AT_NUMBER_POS("수식 형식이 잘못되었습니다. 숫자 위치에 연산자가 있습니다."),

  // 변환 및 연산 관련 오류 메시지
  INVALID_NUMBER_FORMAT("유효하지 않은 숫자 형식입니다: "),
  INVALID_OPERATION_SYMBOL("유효하지 않은 연산자입니다: "),
  DIVISION_BY_ZERO("0으로 나눌 수 없습니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}