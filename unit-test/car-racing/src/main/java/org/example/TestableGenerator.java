package org.example;

import java.util.List;

public class TestableGenerator implements RandomGenerator {
  private final List<Integer> values;
  private int index = 0;

  public TestableGenerator(List<Integer> values) {
    this.values = values;
  }

  @Override
  public int generate() {
    if (index >= values.size()) {
      // 값이 부족하면 예외 대신 마지막 값을 반복하도록 단순 구현
      return values.get(values.size() - 1);
    }
    return values.get(index++);
  }
}