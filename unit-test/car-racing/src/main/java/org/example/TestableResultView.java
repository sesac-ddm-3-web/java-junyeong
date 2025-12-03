package org.example;

import java.util.List;
import java.util.ArrayList;

// RaceResultView의 실제 구현체 대신 테스트에서 사용할 클래스
public class TestableResultView implements RaceResultView { // RaceResultView를 상속하거나 인터페이스를 구현
  // 호출된 인수를 저장할 내부 클래스 (필요하다면)
  public record CurrentResultCall(List<Car> racers, int raceTurn) {}

  private final List<CurrentResultCall> currentResultCalls = new ArrayList<>();
  private final List<List<Car>> winnerCalls = new ArrayList<>();

  // 오버라이드하여 호출 기록을 저장
  @Override
  public void printCurrentResult(List<Car> racers, int raceTurn) {
    // 실제 I/O (System.out)는 발생시키지 않고, 호출 기록만 저장
    currentResultCalls.add(new CurrentResultCall(racers, raceTurn));
  }

  @Override
  public void printWinners(List<Car> winners) {
    winnerCalls.add(winners);
  }

  // 테스트 검증용 Getter
  public List<CurrentResultCall> getCurrentResultCalls() {
    return currentResultCalls;
  }

  public List<List<Car>> getWinnerCalls() {
    return winnerCalls;
  }
}