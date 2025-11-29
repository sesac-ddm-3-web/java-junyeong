package sessac.unit_test;

public enum Operator {
  // 덧셈
  PLUS {
    @Override
    public int calculate(int left, int right) {
      return left + right;
    }
  },

  // 뺄셈
  MINUS {
    @Override
    public int calculate(int left, int right) {
      return left - right;
    }
  },

  // 곱셈
  MULTIPLY {
    @Override
    public int calculate(int left, int right) {
      return left * right;
    }
  },

  // 나눗셈
  DIVIDE {
    @Override
    public int calculate(int left, int right) {
      // 0으로 나누는 경우 예외 처리 (선택 사항)
      if (right == 0) {
        throw new ArithmeticException("0으로 나눌 수 없습니다.");
      }
      return left / right;
    }
  },

  // 나머지 연산
  REMAINDER {
    @Override
    public int calculate(int left, int right) {
      // 0으로 나누는 경우 예외 처리 (선택 사항)
      if (right == 0) {
        throw new ArithmeticException("0으로 나눌 수 없습니다.");
      }
      return left % right;
    }
  };

  public abstract int calculate(int left, int right);

}