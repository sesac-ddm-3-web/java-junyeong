package sessac.unit_test;

import java.util.Random;

public class DiceGame {

  public String dice(int selectValue) {
    if(selectValue % 2 == 0){
      return "성공";
    } else return "실패";
  }

}
