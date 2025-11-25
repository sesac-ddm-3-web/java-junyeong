package spring_practice.simple_board_service.domain;


public class AuthenticationContext {

  private static final ThreadLocal<UserPrincipal> context = new ThreadLocal<>();

  public static void setPrincipal(UserPrincipal userPrincipal) {
    context.set(userPrincipal);
  }

  public static UserPrincipal getPrincipal() {
    return context.get();
  }

  public static void clear() {
    context.remove();
  }

}
