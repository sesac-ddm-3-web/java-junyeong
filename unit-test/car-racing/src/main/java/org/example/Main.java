package org.example;

import org.example.InputView;

public class Main {
  public static void main(String[] args) {
    // 1. 객체 생성 및 의존성 주입
    InputView inputView = new InputView();
    RaceResultView resultView = new RaceResultView();
    RandomGenerator randomGenerator = new ZeroToNineGenerator();

    // 2. 설정 값 입력 받기
    int[] settings = inputView.getGameSettings();
    int carCount = settings[0];
    int raceCount = settings[1];

    // 3. 게임 객체 생성 및 실행
    RacingGame game = new RacingGame(carCount, resultView, randomGenerator);

    game.startGame(raceCount);
  }
}