package sessac.unit_test;

import java.util.Scanner;

public class StringCalculator {

  private String[] operator; // 연산자
  private int[] terms; // 항
  private Scanner sc;

  public int calculate(String input) {
    separateString(input);

    int leftTerm = terms[0];

    for (int i = 0; i < operator.length; i++) {
      String opSymbol = operator[i];

      int rightTerm = terms[i + 1];

      Operator op = Operator.valueOf(opSymbol);

      leftTerm = op.calculate(leftTerm, rightTerm);
    }

    return leftTerm;
  }

  private void separateString(String string) {
    String[] strToArr = sc.nextLine().split(" ");

    for (int i = 0; i < strToArr.length; i++) {
      if (i % 2 == 0) {
        this.terms[i] = Integer.parseInt(strToArr[i]);
      } else {
        this.operator[i] = strToArr[i];
      }
    }
  }

//  public int calculate(String string) {
//    Scanner sc = new Scanner(string);
//
//    String[] inputArray =  sc.nextLine().split(" "); // [ "3" "+" "2" ... ]
//
//    int sum = 0;
//    int[] nums = new int[inputArray.length];
//    String[] operators = new String[inputArray.length];
//
//    for(int i = 0; i < nums.length; i++){
//      if(operators[i].equals("+")){
//        sum += nums[i] + nums[i+1];
//      }
//      else if(operators[i].equals("*")){
//        sum += nums[i] * nums[i+1];
//      }
//      else if(operators[i].equals("/")){
//        sum += nums[i] / nums[i+1];
//      }
//      else if(operators[i].equals("%")){
//        sum += nums[i] % nums[i+1];
//      }
//      else sum = -1;
//    }
//
//    return sum;
//  }

}
