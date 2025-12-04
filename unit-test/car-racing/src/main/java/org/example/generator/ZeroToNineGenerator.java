package org.example.generator;

import java.util.Random;

public class ZeroToNineGenerator implements RandomGenerator {

  private static final Random random = new Random();

  public ZeroToNineGenerator() {}

  @Override
  public int generate() {
    return random.nextInt(10);
  }
}
