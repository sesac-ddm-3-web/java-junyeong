package org.example;

import java.util.List;

public interface RaceResultView {
  void printCurrentResult(List<Car> racers, int raceTurn);
  void printWinners(List<Car> winners);
}