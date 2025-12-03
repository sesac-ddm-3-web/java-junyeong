package org.example;

import org.example.InputView;

public class Main {
  public static void main(String[] args) {
    InputView inputView = new InputView();
    RaceResultView resultView = new ConsoleRaceResultView();
    RandomGenerator randomGenerator = new ZeroToNineGenerator();

    int[] settings = inputView.getGameSettings();
    int carCount = settings[0];
    int raceCount = settings[1];

    RacingGame game = new RacingGame(carCount, resultView, randomGenerator);

    game.startGame(raceCount);
  }
}