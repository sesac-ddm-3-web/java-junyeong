package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Racing 클래스에 대한 단위 테스트.
 * Mockito를 사용하여 RandomGenerator와 RaceResultView의 동작을 제어하고 검증합니다.
 */
class RacingTest {

  private List<Car> racers;
  private RaceResultView mockResultView;
  private RandomGenerator mockGenerator;

  @BeforeEach
  void setUp() {
    // 3대의 자동차 설정
    racers = Arrays.asList(new Car("A"), new Car("B"), new Car("C"));

    // Mock 객체 생성
    mockResultView = mock(RaceResultView.class);
    mockGenerator = mock(RandomGenerator.class);
  }

  @Test
  @DisplayName("4회 경주 시, RaceResultView의 update가 정확히 12번 호출되어야 한다.")
  void race_multipleRounds_callsUpdateCorrectly() {
    // Given
    int raceCount = 4;

    // Generator가 항상 5 (전진 조건 충족)를 반환하도록 설정
    // raceCount * racers.size() = 4 * 3 = 12번의 generate 호출이 예상됨
    when(mockGenerator.generate()).thenReturn(5);

    Racing racing = new Racing(racers, mockResultView, mockGenerator);

    // When
    racing.race(raceCount);

    // Then
    // verify(mockResultView, times(12)).update(anyInt(), anyBoolean());
    // Racing 클래스의 로직에 따라, 12번 모두 true로 호출되었는지 검증
    verify(mockResultView, times(12)).update(anyInt(), eq(true));
    verify(mockResultView, times(1)).printView();
  }

  @Test
  @DisplayName("경주 로직에 따라 전진(true) 및 정지(false) 로직이 정확히 호출되어야 한다.")
  void race_mixedResults_callsUpdateWithCorrectBoolean() {
    // Given
    int raceCount = 1; // 1 라운드만 실행

    // 3대의 자동차가 (전진, 정지, 전진) 순으로 움직이도록 값 설정
    // (4 이상은 GO, 3 이하는 STOP)
    when(mockGenerator.generate())
        .thenReturn(5)  // Car 0: GO (true)
        .thenReturn(2)  // Car 1: STOP (false)
        .thenReturn(8); // Car 2: GO (true)

    Racing racing = new Racing(racers, mockResultView, mockGenerator);

    // When
    racing.race(raceCount);

    // Then
    // InOrder를 사용하여 호출 순서와 인수가 정확한지 검증
    InOrder inOrder = inOrder(mockResultView);

    // Car 0: 5 (>=4) -> true
    inOrder.verify(mockResultView).update(0, true);

    // Car 1: 2 (<4) -> false
    inOrder.verify(mockResultView).update(1, false);

    // Car 2: 8 (>=4) -> true
    inOrder.verify(mockResultView).update(2, true);

    verify(mockResultView, times(1)).printView();
  }
}