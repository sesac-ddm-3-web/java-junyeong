package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    RandomGenerator randomGenerator = new ZeroToNineGenerator();

    String[] input = sc.nextLine().split(" ");
    int carCount = Integer.parseInt(input[0]);
    int raceCount = Integer.parseInt(input[1]);

    String[] readyRacers = {"준영", "현수", "은서", "나현", "찬미", "찬용", "준하", "희찬", "동훈", "지민", "지우", "종균"};

    List<Car> racers = new ArrayList<>();

    for(int i = 0; i < carCount; i++) {
      racers.add(new Car(readyRacers[i]));
    }

    RaceResultView view = new RaceResultView(carCount);

    Racing racing = new Racing(racers, view, randomGenerator);

    racing.race(raceCount);
  }
}
