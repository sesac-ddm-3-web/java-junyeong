package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RacingGameTest {

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

  @SuppressWarnings("unchecked")
  private List<Car> findWinners(RacingGame game) throws Exception {
    Method method = RacingGame.class.getDeclaredMethod("findWinners");
    method.setAccessible(true);
    return (List<Car>) method.invoke(game);
  }


     //   ## 2. 핵심 비즈니스 로직 (Car Movement) 검증

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
    assertThat(racers.get(0).getPosition()).as("준영 위치 (5)").isEqualTo(1);
    assertThat(racers.get(1).getPosition()).as("현수 위치 (2)").isEqualTo(0);
    assertThat(racers.get(2).getPosition()).as("은서 위치 (8)").isEqualTo(1);
  }

  //  3. 우승자 선정 (Winner Selection) 검증

  @Test
  @DisplayName("가장 높은 위치에 있는 자동차(들)을 우승자로 정확히 선정해야 한다.")
  void findWinners_shouldReturnCorrectWinners() throws Exception {
    // Given
    // 1회차: [5, 2, 8] -> [1, 0, 1]
    // 2회차: [5, 2, 8] -> [2, 0, 2]
    TestableGenerator generator = new TestableGenerator(Arrays.asList(5, 2, 8, 5, 2, 8));
    RacingGame game = new RacingGame(FIXED_CAR_COUNT, testableResultView, generator);

    game.startGame(2);

    // When
    List<Car> winners = findWinners(game);

    // Then
    assertThat(winners).as("우승자 수").hasSize(2);
    assertThat(winners.stream().map(Car::getName).toList())
        .as("우승자 이름 목록")
        .containsExactlyInAnyOrder("준영", "은서");
  }

  @Test
  @DisplayName("레이서가 없을 경우 우승자 목록은 비어있어야 한다.")
  void findWinners_noRacers_shouldReturnEmptyList() throws Exception {
    // Given: Car Count 0으로 RacingGame 생성
    RacingGame game = new RacingGame(0, testableResultView, new TestableGenerator(List.of()));

    // When
    List<Car> winners = findWinners(game);

    // Then
    assertThat(winners).as("레이서가 없을 때 우승자 목록").isEmpty();
  }

}