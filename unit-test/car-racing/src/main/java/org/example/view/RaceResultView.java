package org.example.view;

import java.util.List;
import org.example.domain.Car;

public interface RaceResultView {
  void printCurrentResult(List<Car> racers, int raceTurn);
  void printWinners(List<Car> winners);
}