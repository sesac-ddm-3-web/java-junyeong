package sessac.unit_test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiceGameTest {

  @Test
  @DisplayName("짝수면 성공한다.")
  void testDiceSuccess() {
    // 준비
    DiceGame game = new DiceGame(); // POJO라서 가능하다.

    // 실행
    String result = game.dice(2);

    // 검증
    Assertions.assertThat(result).isEqualTo("성공");
  }

  @Test
  @DisplayName("홀수면 실패한다.")
  void testDiceFailure() {
    // 준비
    DiceGame game = new DiceGame(); // POJO라서 가능하다.

    // 실행
    String result = game.dice(3);

    // 검증
    Assertions.assertThat(result).isEqualTo("실패");
  }

}