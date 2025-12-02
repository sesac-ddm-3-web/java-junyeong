package org.example;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum Operation {
  ADD("+", (a, b) -> a + b),
  SUBTRACT("-", (a, b) -> a - b),
  MULTIPLE("*", (a, b) -> a * b),
  DIVISION("/", (a, b) -> {
    if (b == 0) {
      throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
    }
    return a / b;
  });

  private final String symbol;
  private final BiFunction<Integer, Integer, Integer> expression;

  Operation(String symbol, BiFunction<Integer, Integer, Integer> expression) {
    this.symbol = symbol;
    this.expression = expression;
  }

  public static Operation findOperation(String symbol) {
    return Arrays.stream(values())
        .filter(op -> op.symbol.equals(symbol))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 연산자입니다: " + symbol));
  }

  public int apply(int a, int b) {
    return expression.apply(a, b);
  }
}
