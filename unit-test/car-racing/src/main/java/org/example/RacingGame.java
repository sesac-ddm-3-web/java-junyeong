package org.example;

import java.util.ArrayList;
import java.util.List;

public class RacingGame {
  private final List<Car> racers;
  private final RaceResultView view;
  private final RandomGenerator randomGenerator;

  public RacingGame(int carCount, RaceResultView view, RandomGenerator randomGenerator) {
    this.randomGenerator = randomGenerator;
    this.view = view;
    this.racers = initializeRacers(carCount);
  }

  private List<Car> initializeRacers(int count) {
    List<Car> initializedRacers = new ArrayList<>();
    String[] readyRacers = {"준영", "현수", "은서", "나현", "찬미", "찬용", "준하", "희찬", "동훈", "지민", "지우", "종균"};
    for (int i = 0; i < count; i++) {
      initializedRacers.add(new Car(readyRacers[i]));
    }
    return initializedRacers;
  }

  public void startGame(int raceCount) {
    System.out.println("\n실행 결과");
    for (int i = 0; i < raceCount; i++) {
      moveCars();
      view.printCurrentResult(racers, i + 1);
    }

    view.printWinners(findWinners());
  }

  private void moveCars() {
    for (Car car : racers) {
      car.move(randomGenerator.generate());
    }
  }

  private List<Car> findWinners() {
    if (racers.isEmpty()) return new ArrayList<>();

    int maxPosition = racers.stream()
        .mapToInt(Car::getPosition)
        .max()
        .orElse(0);

    return racers.stream()
        .filter(car -> car.getPosition() == maxPosition)
        .toList();
  }
}

