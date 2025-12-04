package org.example.util;

import java.util.List;
import org.example.generator.RandomGenerator;

public class TestableGenerator implements RandomGenerator {
  private final List<Integer> values;
  private int index = 0;

  public TestableGenerator(List<Integer> values) {
    this.values = values;
  }

  @Override
  public int generate() {
    if (index >= values.size()) {
      return values.get(values.size() - 1);
    }
    return values.get(index++);
  }
}