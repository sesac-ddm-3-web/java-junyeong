package org.example.util;

import java.util.List;
import java.util.ArrayList;
import org.example.domain.Car;
import org.example.view.RaceResultView;

public class TestableResultView implements RaceResultView {
  public record CurrentResultCall(List<Car> racers, int raceTurn) {}

  private final List<CurrentResultCall> currentResultCalls = new ArrayList<>();
  private final List<List<Car>> winnerCalls = new ArrayList<>();

  @Override
  public void printCurrentResult(List<Car> racers, int raceTurn) {
    currentResultCalls.add(new CurrentResultCall(racers, raceTurn));
  }

  @Override
  public void printWinners(List<Car> winners) {
    winnerCalls.add(winners);
  }

  public List<CurrentResultCall> getCurrentResultCalls() {
    return currentResultCalls;
  }

  public List<List<Car>> getWinnerCalls() {
    return winnerCalls;
  }
}