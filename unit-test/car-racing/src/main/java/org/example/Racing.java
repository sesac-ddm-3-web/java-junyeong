package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Racing {
  private final List<Car> racers;
  private final RaceResultView raceResultView;

  public Racing(List<Car> racers, RaceResultView raceResultView ) {
    this.racers = racers;
    this.raceResultView = raceResultView;
  }

  public void race(int raceCount) {
    for(int i = 0; i < raceCount; i++) {
      for(int j=0; j<racers.size(); j++) {
        int num = generateRandomZeroToNine();
        raceResultView.update(i, num >= 4);
      }
    }

    raceResultView.printView();
  }

  private Integer generateRandomZeroToNine() {
    Random random = new Random();
    return random.nextInt(10); // 0~9 사이의
  }

}
