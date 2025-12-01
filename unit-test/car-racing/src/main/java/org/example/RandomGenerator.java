package org.example;

// 난수 생성을 위한 함수형 인터페이스
@FunctionalInterface
public interface RandomGenerator {
  /**
   * 0과 9 사이의 정수 난수를 생성합니다.
   */
  int generate();
}