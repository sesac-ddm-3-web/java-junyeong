package org.example;

import java.util.Scanner;

public class InputView {
  private final Scanner scanner = new Scanner(System.in);

  public int[] getGameSettings() {
    System.out.println("자동차 대수와 시도할 횟수를 띄어쓰기로 구분하여 입력해주세요. (예: 5 3)");
    String[] input = scanner.nextLine().split(" ");
    if (input.length != 2) {
      throw new IllegalArgumentException("입력 형식이 올바르지 않습니다.");
    }
    return new int[]{
        Integer.parseInt(input[0]),
        Integer.parseInt(input[1])
    };
  }
}