package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

  private static final String REGEX = "([0-9]+|[-+*/])";

  public int calculate(String input) {
    if (input == null || input.isBlank()) {
      throw new IllegalArgumentException("입력 수식은 비어있을 수 없습니다.");
    }

    // 1. 수식 분리: Pattern/Matcher를 사용하여 숫자와 연산자 토큰만 추출
    List<String> tokens = new ArrayList<>();
    Pattern pattern = Pattern.compile(REGEX);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      tokens.add(matcher.group());
    }

    // 2. 유효한 토큰이 하나도 없는 경우 검사
    if (tokens.isEmpty()) {
      throw new IllegalArgumentException("수식 형식이 잘못되었습니다. 유효한 연산 토큰이 없습니다.");
    }

    // 3. 토큰 패턴 검증: 숫자-연산자-숫자 패턴인지 확인
    validateTokenPattern(tokens);

    // 4. 첫 번째 피연산자를 초기 결과로 설정
    int result = toInt(tokens.get(0));

    // 5. 연산자와 피연산자를 순차적으로 처리
    for (int i = 1; i < tokens.size(); i += 2) {
      String operatorSymbol = tokens.get(i);
      int nextOperand = toInt(tokens.get(i + 1));

      // 6. Operation ENUM에 연산을 위임
      Operation operator = Operation.findOperation(operatorSymbol);
      result = operator.apply(result, nextOperand);
    }

    return result;
  }

  private void validateTokenPattern(List<String> tokens) {
    // 첫 번째 토큰은 숫자여야 함
    if (!isNumber(tokens.get(0))) {
      throw new IllegalArgumentException("수식 형식이 잘못되었습니다. 숫자로 시작해야 합니다.");
    }

    // 토큰은 홀수 개여야 함 (숫자-연산자-숫자 패턴)
    if (tokens.size() % 2 == 0) {
      throw new IllegalArgumentException("수식 형식이 잘못되었습니다. 연산자 뒤에 숫자가 없습니다.");
    }

    // 홀수 인덱스는 연산자, 짝수 인덱스는 숫자여야 함
    for (int i = 0; i < tokens.size(); i++) {
      if (i % 2 == 0) { // 짝수 인덱스: 숫자여야 함
        if (!isNumber(tokens.get(i))) {
          throw new IllegalArgumentException("수식 형식이 잘못되었습니다. 숫자 위치에 연산자가 있습니다.");
        }
      } else { // 홀수 인덱스: 연산자여야 함
        if (isNumber(tokens.get(i))) {
          throw new IllegalArgumentException("수식 형식이 잘못되었습니다. 연산자 위치에 숫자가 있습니다.");
        }
      }
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

  private int toInt(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("유효하지 않은 숫자 형식입니다: " + s);
    }
  }
}