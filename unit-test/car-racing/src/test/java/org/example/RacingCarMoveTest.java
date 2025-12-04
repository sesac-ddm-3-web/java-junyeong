package org.example;

import org.example.domain.Car;
import org.example.domain.RacingGame;
import org.example.util.TestableGenerator;
import org.example.util.TestableResultView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RacingCarMoveTest {

  private TestableResultView testableResultView;
  private final int FIXED_CAR_COUNT = 3;

  @BeforeEach
  void setUp() {
    testableResultView = new TestableResultView();
  }

  @SuppressWarnings("unchecked")
  private List<Car> getRacers(RacingGame game) throws Exception {
    Field field = RacingGame.class.getDeclaredField("racers");
    field.setAccessible(true);
    return (List<Car>) field.get(game);
  }

  @Test
  @DisplayName("moveCars 호출 시 전진 조건(>=4)에 따라 자동차 위치가 정확히 업데이트되어야 한다.")
  void moveCars_mixedResults_shouldUpdatePositionCorrectly() throws Exception {
    // Given
    // move values: 5(GO), 2(STOP), 8(GO)
    TestableGenerator generator = new TestableGenerator(Arrays.asList(5, 2, 8));
    RacingGame game = new RacingGame(FIXED_CAR_COUNT, testableResultView, generator);
    List<Car> racers = getRacers(game);

    // When
    Method moveCarsMethod = RacingGame.class.getDeclaredMethod("moveCars");
    moveCarsMethod.setAccessible(true);
    moveCarsMethod.invoke(game);

    // Then
    assertThat(racers.get(0).getPosition()).as("첫 번째 Car 위치 (5)").isEqualTo(1);
    assertThat(racers.get(1).getPosition()).as("두 번째 Car 위치 (2)").isEqualTo(0);
    assertThat(racers.get(2).getPosition()).as("세 번째 Car 위치 (8)").isEqualTo(1);
  }
}