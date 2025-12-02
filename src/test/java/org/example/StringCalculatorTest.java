package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringCalculatorTest {

  private final StringCalculator calculator = new StringCalculator();

  @ParameterizedTest
  @CsvSource(value = {
      "3+5+4+1+5+6, 24",
      "10-2-3, 5",
      "2*3*4, 24",
      "10/2/2, 2",
      "1+2*3-4/2, 2"
  }, delimiter = ',')
  @DisplayName("쉼표로 구분된 다양한 사칙연산 수식을 순차적으로 계산해야 한다.")
  void calculate_variousExpressions_shouldReturnCorrectResult(String input, int expected) {
    int result = calculator.calculate(input);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("입력 수식이 null이거나 공백일 경우 예외를 던져야 한다.")
  void calculate_nullOrBlankInput_shouldThrowException() {
    // Then
    assertThatThrownBy(() -> calculator.calculate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("입력 수식은 비어있을 수 없습니다.");

    assertThatThrownBy(() -> calculator.calculate(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("입력 수식은 비어있을 수 없습니다.");
  }

  @Test
  @DisplayName("숫자-연산자-숫자 패턴이 아닌 잘못된 수식일 경우 예외를 던져야 한다.")
  void calculate_invalidExpressionFormat_shouldThrowException() {
    assertThatThrownBy(() -> calculator.calculate("3+5+"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("수식 형식이 잘못되었습니다.");

    assertThatThrownBy(() -> calculator.calculate("3++5"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("수식 형식이 잘못되었습니다.");
  }

  @Test
  @DisplayName("0으로 나누는 연산이 포함된 경우 예외를 던져야 한다.")
  void calculate_divisionByZero_shouldThrowException() {
    assertThatThrownBy(() -> calculator.calculate("100/0"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("0으로 나눌 수 없습니다.");

    assertThatThrownBy(() -> calculator.calculate("10*5/0+1"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("0으로 나눌 수 없습니다.");
  }
}