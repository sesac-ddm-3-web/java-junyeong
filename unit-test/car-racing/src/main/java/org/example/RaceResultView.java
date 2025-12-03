package org.example;

import java.util.List;

public class RaceResultView {

  public RaceResultView() {}

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

  public void printWinners(List<Car> winners) {
    String winnerNames = winners.stream()
        .map(Car::getName)
        .reduce((name1, name2) -> name1 + ", " + name2)
        .orElse("우승자가 없습니다.");

    System.out.println("최종 우승자 : " + winnerNames);
  }
}