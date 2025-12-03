package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Racing {

  private final List<Car> racers;
  private final RaceResultView raceResultView;
  private final RandomGenerator randomGenerator;

  public Racing(List<Car> racers, RaceResultView raceResultView, RandomGenerator randomGenerator) {
    this.racers = racers;
    this.raceResultView = raceResultView;
    this.randomGenerator = randomGenerator;
  }

  public void race(int raceCount) {
    for(int i = 0; i < raceCount; i++) {
      for (int j = 0; j < racers.size(); j++) {
        int num = randomGenerator.generate();
        raceResultView.update(j, num >= 4);
      }
      raceResultView.printView();
    }


  }

  private Integer generateRandomZeroToNine() {
    Random random = new Random();
    return random.nextInt(10); // 0~9 사이의
  }

}
