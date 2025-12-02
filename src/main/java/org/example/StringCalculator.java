package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

  private static final String REGEX = "([0-9]+|[-+*/])";
  private final TokenValidator validator = new TokenValidator();

  public int calculate(String input) {
    if (input == null || input.isBlank()) {
      throw new IllegalArgumentException("입력 수식은 비어있을 수 없습니다.");
    }

    List<String> tokens = new ArrayList<>();
    Pattern pattern = Pattern.compile(REGEX);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      tokens.add(matcher.group());
    }

    validator.validate(tokens);

    int result = validator.toInt(tokens.get(0));

    for (int i = 1; i < tokens.size(); i += 2) {
      String operatorSymbol = tokens.get(i);
      int nextOperand = validator.toInt(tokens.get(i + 1));

      Operation operator = Operation.findOperation(operatorSymbol);
      result = operator.apply(result, nextOperand);
    }

    return result;
  }
}
