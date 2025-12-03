package org.example;

import java.util.List;
import java.util.stream.Collectors;

public class ConsoleRaceResultView implements RaceResultView {

  @Override
  public void printCurrentResult(List<Car> racers, int raceTurn) {
    System.out.printf("\n==== %d회차 레이스 결과 ====\n", raceTurn);
    for (Car car : racers) {
      System.out.println(formatCarPosition(car));
    }
    System.out.println();
  }

  private String formatCarPosition(Car car) {
    return car.getName() + " : " + "-".repeat(car.getPosition());
  }

  @Override
  public void printWinners(List<Car> winners) {
    // ... 기존 우승자 출력 로직 ...
    String winnerNames = winners.stream()
        .map(Car::getName)
        .collect(Collectors.joining(", "));

    System.out.println("최종 우승자 : " + (winnerNames.isEmpty() ? "우승자가 없습니다." : winnerNames));
  }
}