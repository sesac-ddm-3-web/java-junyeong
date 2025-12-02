package org.example;

import java.util.List;

public class TokenValidator {

  public void validate(List<String> tokens) {
    if (tokens.isEmpty()) {
      throw new IllegalArgumentException(ErrorCode.EMPTY_TOKENS.getMessage());
    }

    validateTokenPattern(tokens);
  }

  public int toInt(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(ErrorCode.INVALID_NUMBER_FORMAT.getMessage() + s);
    }
  }


  private void validateTokenPattern(List<String> tokens) {
    // --- 1. 초기 검증 ---

    if (!isNumber(tokens.get(0))) {
      throw new IllegalArgumentException(ErrorCode.INVALID_START.getMessage());
    }

    if (tokens.size() % 2 == 0) {
      throw new IllegalArgumentException(ErrorCode.INVALID_END.getMessage());
    }

    // --- 2. 패턴 검증 ---

    for (int i = 0; i < tokens.size(); i += 2) {
      String token = tokens.get(i);
      if (!isNumber(token)) {
        throw new IllegalArgumentException(ErrorCode.OPERATOR_AT_NUMBER_POS.getMessage());
      }
    }

    for (int i = 1; i < tokens.size(); i += 2) {
      String token = tokens.get(i);

      if (isNumber(token)) {
        throw new IllegalArgumentException(ErrorCode.NUMBER_AT_OPERATOR_POS.getMessage());
      }

      if (!isOperator(token)) {
        throw new IllegalArgumentException(ErrorCode.INVALID_OPERATION_SYMBOL.getMessage() + token);
      }
    }
  }

  private boolean isOperator(String token) {
    try {
      Operation.findOperation(token);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private boolean isNumber(String token) {
    try {
      Integer.parseInt(token);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}