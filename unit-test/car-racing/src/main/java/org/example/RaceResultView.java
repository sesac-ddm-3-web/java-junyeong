package org.example;

import java.util.ArrayList;
import java.util.List;

public class RaceResultView {
  public final List<String> view;

  public RaceResultView(Integer carCount) {
    this.view = new ArrayList<>();
    for (int i = 0; i < carCount; i++) {
      this.view.add("-");
    }
  }

  public void update(int carNumber, boolean isGo) {
    if (carNumber >= 0 && carNumber < view.size()) {
      if (isGo) {
        // 현재 위치 문자열을 가져와 하이픈 하나를 추가합니다.
        String currentPosition = this.view.get(carNumber);
        this.view.set(carNumber, currentPosition + "-");
      }
      // 후진(isGo가 false)이면 위치를 변경하지 않습니다.
    } else {
      throw new IllegalArgumentException("유효하지 않은 자동차 번호입니다: " + carNumber);
    }
  }


  public void printView() {
    for(String position : this.view) {
      System.out.println(position);
    }
    System.out.println();
  }
}
